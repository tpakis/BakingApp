package com.scholarship.udacity.aithanasakis.bakingapp.ui.main;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.support.test.espresso.intent.Intents;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.app.Constants;
import com.scholarship.udacity.aithanasakis.bakingapp.ui.details.RecipeDetailsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        //check if on click a correct intent is made
        Intents.init();
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view_recipes_list),
                        childAtPosition(
                                withId(R.id.pull_to_refresh),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));
        intended(hasExtraWithKey(Constants.SELECTEDRECIPE));
        intended(hasComponent(RecipeDetailsActivity.class.getName()));
        Intents.release();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
