package com.example.appcuidadoidosos.viewmodel

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.appcuidadoidosos.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    private lateinit var viewModel: SharedViewModel
    private lateinit var db: AppDatabase
    private lateinit var driver: JdbcSqliteDriver
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        db = AppDatabase(driver)
        viewModel = SharedViewModel(db)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `addWater inserts a water log`() = runTest {
        // Initial count
        assertEquals(0, db.waterQueries.selectAllWater().executeAsList().size)

        // Add water
        viewModel.addWater()
        testDispatcher.scheduler.advanceUntilIdle() // Process the coroutine

        // Verify count updated
        assertEquals(1, db.waterQueries.selectAllWater().executeAsList().size)
    }

    @Test
    fun `deleteWater removes the water log`() = runTest {
        viewModel.addWater()
        testDispatcher.scheduler.advanceUntilIdle()
        val log = db.waterQueries.selectAllWater().executeAsOne()
        assertEquals(1, db.waterQueries.selectAllWater().executeAsList().size)

        viewModel.deleteWater(log.id)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, db.waterQueries.selectAllWater().executeAsList().size)
    }

    @Test
    fun `addMeal inserts a meal log`() = runTest {
        assertEquals(0, db.mealQueries.selectAllMeals().executeAsList().size)

        viewModel.addMeal("Almoço", "Arroz e feijão")
        testDispatcher.scheduler.advanceUntilIdle()

        val meals = db.mealQueries.selectAllMeals().executeAsList()
        assertEquals(1, meals.size)
        assertEquals("Almoço", meals.first().type)
    }

    @Test
    fun `addMedication inserts a medication`() = runTest {
        assertEquals(0, db.medicationQueries.selectAllMedications().executeAsList().size)

        viewModel.addMedication("Paracetamol", "08:00")
        testDispatcher.scheduler.advanceUntilIdle()

        val meds = db.medicationQueries.selectAllMedications().executeAsList()
        assertEquals(1, meds.size)
        assertEquals("Paracetamol", meds.first().name)
        assertFalse(meds.first().isTaken)
    }

    @Test
    fun `toggleMedicationTaken updates the status`() = runTest {
        viewModel.addMedication("Ibuprofeno", "12:00")
        testDispatcher.scheduler.advanceUntilIdle()
        val med = db.medicationQueries.selectAllMedications().executeAsOne()
        assertFalse(med.isTaken)

        // Toggle to true
        viewModel.toggleMedicationTaken(med.id, true)
        testDispatcher.scheduler.advanceUntilIdle()
        val updatedMed = db.medicationQueries.selectAllMedications().executeAsOne()
        assertTrue(updatedMed.isTaken)

        // Toggle to false
        viewModel.toggleMedicationTaken(med.id, false)
        testDispatcher.scheduler.advanceUntilIdle()
        val finalMed = db.medicationQueries.selectAllMedications().executeAsOne()
        assertFalse(finalMed.isTaken)
    }
}