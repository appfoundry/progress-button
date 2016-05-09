## Attributes

* **fillColor** (color) - Set the background color of the button.

    ```
    progressbutton:fillColor="#ffcd3a3a"
    ```
    
* **progressColor** (color) - Set the color of the progress indicator.

    ```
    progressbutton:progressColor="@color/colorPrimary"
    ```
    
* **strokeColor** (color) - Set the color of the border.

    ```
    progressbutton:strokeColor="#FF1010"
    ```
    
* **strokeWidth** (dimens) - Set the border width.

    ```
    progressbutton:strokeWidth="10dp"
    ```
    
* **progressIcon** (drawable) - Set an icon on the button.

    ```
    progressbutton:progressIcon="@drawable/ic_settings_remote_white_36dp"
    ```
    
* **indeterminate** (boolean) - Set whether the button is indeterminate or determinate.

    ```
    progressbutton:indeterminate="true"
    ```
    

## Methods

* **setIndeterminate(boolean isIndeterminate)** - Set whether the button is indeterminate or determinate.

    ```
    progressButton.setIndeterminate(true);
    ```
    
* **setAnimationStep(float animationStep)** - Set the amount of progress for each animation step.

    ```
    progressButton.setAnimationStep(3);
    ```

* **setAnimationDelay(float animationDelay)** - Set the delay in milliseconds after each animation step to determine the speed of the animation.

    ```
    progressButton.setAnimationDelay(0);
    ```
    
* **startAnimating()** - Start the indeterminate progress animation.

    ```
    progressButton.startAnimating();
    ```
    
* **stopAnimating()** - Stop the indeterminate progress animation.

    ```
    progressButton.stopAnimating();
    ```
    
* **setMaxProgress(float maxProgress)** - Set the maximum progress value for determinate progress.

    ```
    progressButton.setMaxProgress(10.0f);
    ```
    
* **setProgress(float progress)** - Set the progress value for determinate progress.

    ```
    progressButton.setProgress(5.0f);
    ```
        
* **setStartDegrees(float startDegrees)** - Set the starting point for the progress indicator. (0 for left, 90 for bottom, ...)

    ```
    progressButton.setStartDegrees(270);
    ```
    
* **setIcon(Drawable icon)** - Set icon on the button.

    ```
    progressButton.setIcon(getResources().getDrawable(R.drawable.icon));
    ```
            
* **setRadius(float radius)** - Set the radius of the button in dp.

    ```
    progressButton.setRadius(70f);
    ```
            
* **setStrokeWidth(float strokeWidth)** - Set the stroke width of the button in dp.

    ```
    progressButton.setStrokeWidth(10f);
    ```
           
* **setColor(int color)** - Set the background color of the button.

    ```
    progressButton.setColor(Color.parseColor("#117700"));
    ```
          
* **setStrokeColor(int color)** - Set the stroke color of the button.

    ```
    progressButton.setStrokeColor(Color.parseColor("#007777"));
    ```
      
* **setProgressColor(int color)** - Set the color of the progress indicator.

    ```
    progressButton.setProgressColor(Color.parseColor("#007777"));
    ```