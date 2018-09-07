package com.mytaxi.android_demo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytaxi.android_demo.activities.DriverProfileActivity;
import com.mytaxi.android_demo.activities.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by DilshanF on 9/7/2018.
 */

@RunWith(AndroidJUnit4.class)
public class DriverProfileActivityTest {

    @Rule
    public ActivityTestRule<DriverProfileActivity> mActivityRule = new ActivityTestRule<>(
            DriverProfileActivity.class);
}
