package by.yura.drawing;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawView;
    private Button button;

    private ImageButton pen;
    private ImageButton erase;
    private ImageButton shape;

    private int status = 0;
    Drawable drawable;

    @SuppressLint({"ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);
        button = findViewById(R.id.colorBtn);
        drawView.setButton(button);
        button.setBackgroundColor(Color.BLACK);
        pen = findViewById(R.id.draw_btn);
        erase = findViewById(R.id.erase_btn);
        shape = findViewById(R.id.shapes_btn);

        try {
            drawable = Drawable.createFromXml(getResources(), getResources().getXml(R.drawable.btn_border));
            pen.setForeground(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException ignored) { }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawView.recreate();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onErase(View view) {
        drawView.setErase(true);
        status = 0;
        drawView.setStatus(0);

        pen.setForeground(null);
        shape.setForeground(null);
        erase.setForeground(drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onDraw(View view) {
        drawView.setErase(false);
        status = 0;
        drawView.setStatus(0);

        pen.setForeground(drawable);
        shape.setForeground(null);
        erase.setForeground(null);
    }

    public void onColor(View view) {
        drawView.setPaintColor(view.getTag().toString());
    }

    public void onShape(View view) {

        String[] items = {"Прямоугольник", "Круг", "Треугольник"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Выбрать фигуру")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("TAG", "onClick: " + i);
                        switch (i) {
                            case 0:
                                status = 1;
                                drawView.setStatus(1);
                                break;
                            case 1:
                                status = 2;
                                drawView.setStatus(2);
                                break;
                            case 2:
                                status = 3;
                                drawView.setStatus(3);
                                break;
                        }

                        pen.setForeground(null);
                        shape.setForeground(drawable);
                        erase.setForeground(null);
                        drawView.setErase(false);

                    }
                }).setPositiveButton("ОК", null);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();




    }

    public void onNew(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 800);

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                drawView.loadFile(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSize(View view) {

        String[] items = {"Малый", "Средний", "Большой"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Выбрать размер кисти")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("TAG", "onClick: " + i);
                        switch (i) {
                            case 0:
                                drawView.setSize(0);
                                break;
                            case 1:
                                drawView.setSize(1);
                                break;
                            case 2:
                                drawView.setSize(2);
                                break;
                        }
                    }
                }).setPositiveButton("ОК", null);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }
}
