# StoryLayout for Android

We can see that images story and video story become craze and common use case in the daily used application,
like WhatsApp status, YouTube shorts, Instagram story etc. With inspiration of them, [StoryLayout] designed. 

`StoryLayout` is a child class of ![ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout),
with help ![ContDownTimer](https://developer.android.com/reference/android/os/CountDownTimer) and 
![ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2) achieved this common use case for the future apps.
`StoryLayout` easy to use in your Android app. You can simply declare in your view and set your custom pager adapter with your app suitable 
UI design. 

<img src="https://github.com/riontech-xten/StoryLayout/blob/main/ezgif.com-video-to-gif.gif" width="250"/>

## Usage

Below is a fast guide to getting started.

### Import module in your app build.gradle 

```
dependencies {
    implementation project(path: ':storylayout')
    ...
}
```

### Add StoryLayout in your xml

```
<com.codeglo.storylayout.StoryLayout
            android:id="@+id/storyLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/white"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            story:delayTime="15000"
            story:indHorizontalPadding="@dimen/screen_padding"
            story:indVerticalPadding="@dimen/view_margin"
            story:prgIndColor="@color/white"
            story:prgIndSpace="@dimen/std_4dp">
```

### Customize the progress and story play time 

```delayTime``` - Maximum story playing time. 
```indHorizontalPadding``` - Progress indicator horizontal padding.
```indVerticalPadding``` - Progress indicator vertical padding.
```prgIndColor``` - Progress indicator color.
```prgIndSpace``` - Progress indicator between space.
```prgAllowCircular``` - To allow repeat all story

You can also set all attributes programmatically

```
binding.storyLayout.prgIndColor = Color.WHITE
binding.storyLayout.prgIndSpace = 4f
binding.storyLayout.prgIndHorizontalPadding = 8f
binding.storyLayout.prgIndVerticalPadding = 4f
binding.storyLayout.prgAllowCircular = false
```

### Setup story pages

Create a pager adapter
```
class StoryPagerAdapter(
    private val manager: FragmentManager,
    lifecycle: Lifecycle,
    private val deals: List<DealStory>
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (deals[position].storyType == StoryType.VIDEO.value)
            VideoStoryFragment.newInstance(deals[position], position)
        else ImageStoryFragment.newInstance(deals[position], position)
    }

    fun getStory(position: Int) = deals[position]

    fun videoStoryFragmentAt(position: Int): VideoStoryFragment? {
        val fragment = manager.findFragmentByTag("f$position")
        if (fragment is VideoStoryFragment)
            return fragment
        return null
    }
}
```

Set your pager adapter in your [StoryLayout]

```
StoryPagerAdapter storyPagerAdapter = StoryPagerAdapter(childFragmentManager, lifecycle, deals)
binding.storyLayout.adapter = storyPagerAdapter
```

[StoryLayout] adapter is a instance of ![Recycler.Adapter](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter) and with the use of
![ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2) we can set the horizontal pager with help of ![FragmentStateAdapter](https://developer.android.com/reference/kotlin/androidx/viewpager2/adapter/FragmentStateAdapter)
or vertical pager with help of ![Recycler.Adapter](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter), here we have use the
![FragmentStateAdapter](https://developer.android.com/reference/kotlin/androidx/viewpager2/adapter/FragmentStateAdapter) which is a child class of ![Recycler.Adapter](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter). 


### Implement OnStoryChangeListener

Implement [OnStoryChangeListener] in your [Fragment] or [Activity] and override it's methods to 
handle user actions. Now assign to [StoryLayout.StoryChangeListener]

```
binding.storyLayout.storyChangeListener = this
```

After implemented [OnStoryChangeListener] your class look like

```
class DealFragment : Fragment(R.layout.fragment_deal), OnStoryChangeListener {
    private lateinit var binding: FragmentDealBinding
    private lateinit var storyPagerAdapter: StoryPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDealBinding.bind(view)
    }

    private fun bindStoryPager(deals: List<DealStory>) {
        binding.storyLayout.storyChangeListener = this
        storyPagerAdapter = StoryPagerAdapter(childFragmentManager, lifecycle, deals)
        binding.storyLayout.adapter = storyPagerAdapter
        ...
    }

    override fun onNext(position: Int) {
        binding.deal = storyPagerAdapter.getStory(position)
        binding.storyLayout.delayTime = 15000
    }

    override fun onPrevious(position: Int) {
        binding.deal = storyPagerAdapter.getStory(position)
        binding.storyLayout.delayTime = 15000
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.resetVideo()
        }
    }

    override fun onHold(position: Int) {
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.pauseVideo()
        }
    }

    override fun onRelease(position: Int) {
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.resumePlayer()
        }
    }

    override fun onRestartStory(position: Int) {
        findNavController().popBackStack()
    }

    override fun onSwipeLeftToRight(position: Int) {
        super.onSwipeLeftToRight(position)
        requireContext().showToast("You swiped Left => Right")
    }

    override fun onSwipeRightToLeft(position: Int) {
        super.onSwipeRightToLeft(position)
        requireContext().showToast("You swiped Right => Left")
    }

    override fun onSwipeUp(position: Int) {
        super.onSwipeUp(position)
        requireContext().showToast("You swiped up")
    }

    override fun onSwipeDown(position: Int) {
        super.onSwipeDown(position)
        requireContext().showToast("You swiped down")
    }
}
```

To know more about [StoryLayout] see the demo project [StoryLayoutDemo].

Enjoy coding!

License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.