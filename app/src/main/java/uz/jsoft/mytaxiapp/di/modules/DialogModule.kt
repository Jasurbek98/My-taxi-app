package uz.jsoft.mytaxiapp.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.jsoft.mytaxiapp.presentation.ui.dialogs.SavedSearchBottomSheet
import javax.inject.Singleton

/**
 * Created by Jasurbek Kurganbayev 03/04/2022
 */

@Module
@InstallIn(SingletonComponent::class)
class DialogModule {

    @Provides
    @Singleton
    fun getSearchLayoutMaker(): SavedSearchBottomSheet = SavedSearchBottomSheet

//    @Provides
//    @Singleton
//    fun getDestinationLayoutMaker(): DestinationLocationBottomSheet = DestinationLocationBottomSheet

}