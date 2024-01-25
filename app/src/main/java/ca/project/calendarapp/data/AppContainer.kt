package ca.project.calendarapp.data

import android.content.Context

interface AppContainer {
    val itemsRepository: EventsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: EventsRepository by lazy {
        OfflineEventsRepository(InventoryDatabase.getDatabase(context).eventDao())
    }
}

