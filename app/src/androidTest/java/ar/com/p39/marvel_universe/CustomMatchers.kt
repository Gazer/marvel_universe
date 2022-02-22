package ar.com.p39.marvel_universe

import android.R
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf


object CustomMatchers {
    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun withRecyclerView(@IdRes viewId: Int): Matcher<View?>? {
        return allOf(isAssignableFrom(RecyclerView::class.java), withId(viewId))
    }

    fun onRecyclerItemView(
        @IdRes identifyingView: Int,
        identifyingMatcher: Matcher<View?>?,
        childMatcher: Matcher<View?>?
    ): ViewInteraction? {
        val itemView: Matcher<View> = allOf(
            withParent(withRecyclerView(R.id.list)),
            withChild(allOf(withId(identifyingView), identifyingMatcher))
        )
        return Espresso.onView(allOf(isDescendantOfA(itemView), childMatcher))
    }
}