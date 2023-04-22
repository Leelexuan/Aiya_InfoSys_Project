package com.example.aiya_test_3.incidents.Activities;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public abstract class BackgroundTasks <I, O> {
    /*******
     * This abstract class uses the template method design pattern. -> to be implemented in the activity.
     * The start method contains all code necessary to run one background task
     * and display its result on an Android activity. (algorithm)
     *
     * The task method handles the background task
     *
     * The done method handles the UI thread task.
     */

    abstract public O task(I input);
    /*******
     * abstract method for the background task.
     * a generic class I, userInput
     * the output of this task, with generic object O will be passed into the method done
     * to run the ui method.
     */

    abstract public void done(O result);
    /************
     * abstract method for the task on the android UI thread
     * carried out after the background task is completed
     */


    public void start(final I userInput) {
        /******
         * The start method encompasses the algorithm to perform background and ui thread tasks.
         *
         * Inputs: a generic class I, userInput
         * */

        // UI task //
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Background Task //
                final Container<O> container = new Container<>();
                O o = task(userInput);
                container.set(o);

                // UI Task - Because of the handler object //
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        O o = container.get();
                        if( o != null){
                            done(o);
                        }
                    }
                });
            }
        });
    }


    /**********
     * Container class to allow anonymous inner class to read and write to local variables
     * T is a generic object that we can get and set to the container.
     */
    public static class Container<T> {

        T value;

        Container() {
            this.value = null;
        }

        void set(T t) {
            this.value = t;
        }

        T get() {
            return this.value;
        }
    }
}
