package com.example.appcuidadoidosos.viewmodel

import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.notifier.FakeNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedViewModelTest {

    private lateinit var viewModel: SharedViewModel
    private lateinit var database: AppDatabase
    private lateinit var notifier: FakeNotifier
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        val driver = DatabaseDriverFactory().createDriver()
        database = AppDatabase(driver)
        notifier = FakeNotifier()
        viewModel = SharedViewModel(database, notifier, CoroutineScope(mainThreadSurrogate))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `addMedication schedules a notification`() = runTest {
        viewModel.addMedication("Test Med", "1 pill", "daily", "08:00", null)
        assertEquals(1, notifier.notifications.size)
        assertEquals("Lembrete de Medicação", notifier.notifications.first().title)
    }

    @Test
    fun `addMedication saves item to database`() = runTest {
        viewModel.addMedication("Test Med", "1 pill", "daily", "08:00", null)
        val medications = database.appDatabaseQueries.selectAllMedications().executeAsList()
        assertEquals(1, medications.size)
        assertEquals("Test Med", medications.first().name)
    }

    @Test
    fun `deleteMedication cancels notification`() = runTest {
        // First, add a medication to get a valid notification ID
        viewModel.addMedication("Test Med", "1 pill", "daily", "08:00", null)
        val medication = database.appDatabaseQueries.selectAllMedications().executeAsOne()
        
        // Now, delete it
        viewModel.deleteMedication(medication.id)
        
        assertEquals(1, notifier.cancelledNotifications.size)
        assertEquals(medication.notification_id, notifier.cancelledNotifications.first())
    }

    @Test
    fun `deleteMedication removes item from database`() = runTest {
        // First, add a medication
        viewModel.addMedication("Test Med", "1 pill", "daily", "08:00", null)
        var medications = database.appDatabaseQueries.selectAllMedications().executeAsList()
        assertEquals(1, medications.size)

        // Now, delete it
        viewModel.deleteMedication(medications.first().id)

        medications = database.appDatabaseQueries.selectAllMedications().executeAsList()
        assertTrue(medications.isEmpty())
    }
}
