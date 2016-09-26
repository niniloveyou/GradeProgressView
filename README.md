# GradeProgressView
a progress view used to grade mobiles phone

![](https://github.com/niniloveyou/GradeProgressView/blob/master/gradeProgressView.gif)

#System Requirement

Android API 11+， Because of the use of ValueAnimation， if you want use it on API 8, please use NineoldAndroids library in your project.

#Usage
        
        gradeProgressView.setProgress(100);                 //without animation
        
        gradeProgressView.setProgressWidthAnimation(100);   // with animation
        
        gradeProgressView.setBackgroundColor(Color.LTGRAY); //set background color
        
        gradeProgressView.setProgressColor(Color.WHITE);    //set progress and pointer color
        
        gradeProgressView.setLineWidth(60);                 //set the progress width
        
        gradeProgressView.setGapWidth(30);                  //set gap between out line and progress line
        
        gradeProgressView.setOutLineWidth(5);               //set out line widht
        
        gradeProgressView.setOnProgressChangeListener(new GradeProgressView.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(GradeProgressView gradeProgressView, int progress) {
                Log.i("GradeProgressView test", "on progress changed: " + progress);
            }
        });
        
