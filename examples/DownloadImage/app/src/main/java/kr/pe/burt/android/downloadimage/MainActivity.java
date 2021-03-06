package kr.pe.burt.android.downloadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.pe.burt.android.lib.androidoperationqueue.AndroidOperation;
import kr.pe.burt.android.lib.androidoperationqueue.AndroidOperationQueue;
import kr.pe.burt.android.lib.androidoperationqueue.Operation;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    AndroidOperationQueue queue = new AndroidOperationQueue("DownloadQueue");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.downloadButton)
    void onDownloadButtonClicked() {

        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue queue, Bundle bundle) {
                AndroidOperation.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue queue, Bundle bundle) {
                bundle.putString("url", "https://raw.githubusercontent.com/skyfe79/AndroidOperationQueue/master/examples/art/marvel01.jpeg");
            }
        });

        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue queue, Bundle bundle) {

                String url = bundle.getString("url", null);
                if(url == null) {
                    queue.stop();
                }

                final Bitmap bitmap = downloadBitmap(url);

                if(bitmap != null) {
                    AndroidOperation.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
        queue.addOperation(new Operation() {
            @Override
            public void run(AndroidOperationQueue queue, Bundle bundle) {
                AndroidOperation.sleep(1000);
                AndroidOperation.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        queue.start();
    }




    private Bitmap downloadBitmap(String url) {

        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            Log.d("downloadBitmap", e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("downloadBitmap", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return null;
    }
}
