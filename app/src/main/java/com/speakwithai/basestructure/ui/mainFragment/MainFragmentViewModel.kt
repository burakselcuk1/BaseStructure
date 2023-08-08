package com.speakwithai.basestructure.ui.mainFragment

import androidx.lifecycle.LiveData
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.BillingManager
import com.speakwithai.basestructure.common.enums.UserStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val billingManager: BillingManager): BaseViewModel() {

    val userStatus: LiveData<UserStatus> get() = billingManager.userStatus


}