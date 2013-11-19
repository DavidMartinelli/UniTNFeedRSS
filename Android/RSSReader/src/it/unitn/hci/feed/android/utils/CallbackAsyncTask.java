package it.unitn.hci.feed.android.utils;

import android.os.AsyncTask;

public abstract class CallbackAsyncTask<T> extends AsyncTask<Void, Void, T>
{

    private final Action<TaskResult<T>> mCallback;
    private Exception mException;


    public CallbackAsyncTask(Action<TaskResult<T>> callback)
    {
        mCallback = callback;
    }


    @Override
    protected T doInBackground(Void... params)
    {
        try
        {
            return doJob();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            mException = e;
            return null;
        }
    }


    public abstract T doJob() throws Exception;


    @Override
    protected void onPostExecute(T result)
    {
        mCallback.invoke(new TaskResult<T>(result, mException));
    }

    public static class TaskResult<E>
    {
        public E result;
        public Exception exception;


        public TaskResult(E result, Exception e)
        {
            this.result = result;
            this.exception = e;
        }
    }

    public static interface Action<T>
    {
        public void invoke(T param);
    }

}
