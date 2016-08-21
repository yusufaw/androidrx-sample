package apps.crevion.com.samplerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PLAYGROUND";
    TextView textView;
    Observable<Integer> integerObservable = Observable.just(4, 8, 15, 16, 23, 42);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_number);

//        integerObservable.subscribe(integerObserver);

//        integerObservable.subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                Log.d(TAG, "call: " + integer);
//            }
//        });

//        integerObservable
//                .map(new Func1<Integer, String>() {
//                    @Override
//                    public String call(Integer integer) {
//                        return Integer.toBinaryString(integer);
//                    }
//                })
//                .filter(new Func1<String, Boolean>() {
//                    @Override
//                    public Boolean call(String s) {
//                        return s.endsWith("1");
//                    }
//                })
//                .map(new Func1<String, Integer>() {
//                    @Override
//                    public Integer call(String s) {
//                        return Integer.parseInt(s, 2);
//                    }
//                })
//                .map(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        return integer * integer;
//                    }
//                }).subscribe(integerObserver);

        final Subscription subscription = computerNumberObservable.
                subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerObserver);

        Button startRxOperationButton = (Button) findViewById(R.id.start_btn);
        startRxOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view.setEnabled(false);
                subscription.unsubscribe();
            }
        });
    }

    Observer<Integer> integerObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: ", e);
        }

        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "onNext: " + integer);
            textView.setText(integer.toString());


        }
    };

    Observable<Integer> computerNumberObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            int i = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }

                subscriber.onNext(i++);

                if(i == 10){
                    break;
                }
            }
            subscriber.onCompleted();
        }
    });
}
