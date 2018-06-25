# Demo

![Alt text](https://raw.githubusercontent.com/PaduaPunk/audio-recoder-visualizer/master/ezgif-4-57d0df1ef2.gif)


# How to use
 
```xml 
 <com.chemapadua.audiorecodervisualizer.visualizer.BarVisualizer
                    android:id="@+id/visualizer"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_weight=".8" />


```
In your java class

BarVisualizer barVisualizer = findViewById(R.id.visualizer);

// set custom color to the line.

barVisualizer.setColor(ContextCompat.getColor(this, R.color.you_color));

// define custom number of bars you want in the visualizer between (10 - 256).

barVisualizer.setDensity(70);

// For start visualizer. 

barVisualizer.start();

// For stop visualizer. 

barVisualizer.start();

# Note
This is an alpha version not recomended for production

License
=======
Copyright 2018 Chema Padua based on code from Gautam Chibde
https://github.com/GautamChibde/android-audio-visualizer/wiki/Bar-Visualizer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
