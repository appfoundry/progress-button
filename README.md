# ProgressButton
A circular progress button for Android

![](screenshots/screenshot.gif)

## Integration

```
dependencies {
    compile 'be.appfoundry.progressbuttonlibrary.progress-button:1.0'
}
```

## Usage

````
<be.appfoundry.sampleapp.progressbutton.ProgressButton
        android:id="@+id/progressButton"
        android:layout_width="120dp"
        android:layout_height="120dp"
        progressbutton:fillColor="#ffcd3a3a"
        progressbutton:progressColor="@color/colorPrimary"
        progressbutton:strokeWidth="10dp"
        progressbutton:strokeColor="#FF1010"
        progressbutton:progressIcon="@drawable/ic_settings_remote_white_36dp"
        android:layout_gravity="center"/>
```

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
