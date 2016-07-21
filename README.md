# JellyPageIndicator
Paging indicator widgets that are compatible with the ViewPager but INDEPENDENT which provide a few styles when scrolled such as jelly.
![gif](https://raw.githubusercontent.com/ChrisCheng4j/JellyPageIndicator/master/img/demo.gif)
##Integration
Add the dependencies to your gradle file:
```javascript
dependencies {
    compile 'com.chrischeng:pageindicator:0.9.1'
}
```
##XML Usage
-    Use directly:
```xml
<android.support.v4.view.ViewPager
    android:id="@+id/vp"
    android:layout_width="match_parent"
    android:layout_height="150dp" />

<com.chrischeng.pageindicator.CirclePageIndicator
    android:id="@+id/cpi"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignBottom="@id/vp"
    android:layout_centerHorizontal="true"
    android:layout_marginBottom="8dp" />
```
-   Current Attributes supported:
```xml
<attr name="android:orientation" />
    <attr name="pi_circle_radius" format="dimension" />
    <attr name="pi_circle_spacing" format="dimension" />
    <attr name="pi_circle_stroke_width" format="dimension" />
    <attr name="pi_circle_stroke_color" format="color" />
    <attr name="pi_circle_normal_color" format="color" />
    <attr name="pi_circle_selected_color" format="color" />
    <attr name="android:background" />
    <attr name="pi_circle_count" format="integer" />
    <attr name="pi_circle_single_show" format="boolean" />
    <attr name="pi_scroll_style" format="enum">
        <enum name="selected" value="0" />
        <enum name="swipe" value="1" />
        <enum name="jelly" value="2" />
    </attr>
    <attr name="pi_jelly_radius_min" format="dimension" />
```
##Java Usage
```java
viewPager.setAdapter(adapter);
mIndicator.setCount(adapter.getCount());
viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.onPageSelected(position);
    }

    @Override
        public void onPageScrollStateChanged(int state) {
        mIndicator.onPageScrollStateChanged(state);
    }
});
```
##Thanks
[Jake Warthon][ViewPagerIndicator] for implementation of indicator widget.

[chenupt][SpringIndicator] for implementation of jelly effect.
## License
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[ViewPagerIndicator]:https://github.com/JakeWharton/ViewPagerIndicator
[SpringIndicator]:https://github.com/chenupt/SpringIndicator
