package example.tacademy.com.sampledata;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor>{

    String[] projection = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA};

    String sort = MediaStore.Images.Media.DATE_ADDED + " DESC";
    GridView gridView;
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        gridView = (GridView)findViewById(R.id.gridView);
        String[] from = {MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA};
        int[] to = {R.id.text_name, R.id.image_icon};
        mAdapter = new SimpleCursorAdapter(this, R.layout.view_image, null, from, to, 0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == dataColumnIndex){
                    String path = cursor.getString(columnIndex);
                    ImageView iv = (ImageView)view;
                    Glide.with(ImageViewActivity.this)
                            .load(new File(path))
                            .into(iv);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0,null,this);
    }

    private int dataColumnIndex = -1;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
