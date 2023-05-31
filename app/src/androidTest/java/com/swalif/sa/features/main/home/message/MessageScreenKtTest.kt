package com.swalif.sa.features.main.home.message

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.MainActivity
import com.swalif.sa.ui.theme.ChatAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
internal class MessageScreenKtTest{



    @get:Rule(order = 1)
     var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun setup(){
        hiltTestRule.inject()
        composeRule.setContent {
            ChatAppTheme() {
                MessageScreen(navController = rememberNavController())
            }
        }
    }

    @Test
    fun messageTest(){
        composeRule.onNodeWithContentDescription("email").assertDoesNotExist()
    }
}