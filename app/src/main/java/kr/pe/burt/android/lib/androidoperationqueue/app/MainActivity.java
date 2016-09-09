package kr.pe.burt.android.lib.androidoperationqueue.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import kr.pe.burt.android.lib.androidoperationqueue.AndroidOperation;
import kr.pe.burt.android.lib.androidoperationqueue.AndroidOperationQueue;
import kr.pe.burt.android.lib.androidoperationqueue.Operation;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    AndroidOperationQueue queue = new AndroidOperationQueue("MyQueue");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textview);

        for(int i = 1; i<=10; i++) {
            final int count = i;
            queue.addOperation(new Operation() {
                @Override
                public void run(final AndroidOperationQueue queue, final Bundle bundle) {

                    AndroidOperation.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.valueOf(count));
                        }
                    });

                    AndroidOperation.sleep(1000);
                }

                @Override
                public void cancel() {

                }

                @Override
                public String getTag(String identify) {
                    return identify;
                }
            });
        }
        queue.start();

        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue q, Bundle bundle) {
                bundle.putInt("sum", 1);
            }

            @Override
            public void cancel() {

            }

            @Override
            public String getTag(String identify) {
                return identify;
            }
        });

        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue q, Bundle bundle) {
                int sum = bundle.getInt("sum");
                bundle.putInt("sum", sum + 2);
            }

            @Override
            public void cancel() {

            }

            @Override
            public String getTag(String identify) {
                return identify;
            }
        });
        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue q, Bundle bundle) {
                int sum = bundle.getInt("sum");
                bundle.putInt("sum", sum + 3);
            }

            @Override
            public void cancel() {

            }

            @Override
            public String getTag(String identify) {
                return identify;
            }
        });
        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue q, Bundle bundle) {
                int sum = bundle.getInt("sum");
                Log.v("SUM", String.format("1+2+3 = %d", sum));
            }

            @Override
            public void cancel() {

            }

            @Override
            public String getTag(String identify) {
                return identify;
            }
        });
    }
}
