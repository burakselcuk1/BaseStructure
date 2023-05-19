package com.example.basestructure.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentChatBinding


class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(
    layoutId = R.layout.fragment_chat,
    viewModelClass = ChatViewModel::class.java
) {
    override fun onInitDataBinding() {

    }


}