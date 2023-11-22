package com.example.googlemap2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    private Button button1;
    private Button button2;

    //현재 위도 & 경도 임의 설정
    private double current_latitude = 37.5;
    private double current_hardness = 127.0;
    List<Marker> markerList = new ArrayList<>();
    int[] n;
    int tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);

        // n 배열 초기화
        n = new int[5];
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng[] locations = {
                new LatLng(37.5642135,127.0016985),
                new LatLng(37.532610938096, 126.99448332375),
                new LatLng(37.554371328, 126.9227542239),
                new LatLng(37.50042,127.06418),
                new LatLng(37.544023739723215,127.05247047608701)
        };

        Marker myLocation = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(current_latitude,current_hardness))
                .title("현재 위치"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation.getPosition()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation.getPosition(), 23));

        for (int i = 0; i < locations.length; i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(locations[i])
                    .snippet(n[i] + "개 남았습니다.")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("custom_marker", 115, 115))));

            if (i==0)
                marker.setTitle("서울 중심");
            else if (i==1)
                marker.setTitle("이태원");
            else if (i==2)
                marker.setTitle("홍대");
            else if (i==3)
                marker.setTitle("대치동");
            else if (i==4)
                marker.setTitle("성수동");
            markerList.add(marker);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerList.contains(marker)) {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);

            // 클릭한 마커의 인덱스
            tmp = markerList.indexOf(marker);
            // 여기에 필요한 로직 추가
            marker.showInfoWindow();
            return true;
        }
        return false;
    }
    private Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
    public void onButton1Click(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("개수 입력");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputValue = input.getText().toString();

                try {
                    int intValue = Integer.parseInt(inputValue);
                    Toast.makeText(MainActivity.this, "넣은 개수: " + intValue, Toast.LENGTH_SHORT).show();
                    n[tmp] += intValue;

                    // 기존 마커의 snippet 업데이트
                    Marker marker = markerList.get(tmp);
                    if (n[tmp] > 9) {
                        marker.setSnippet("10개 이상 남았습니다.");
                    } else {
                        marker.setSnippet(n[tmp] + "개 남았습니다.");
                    }
                    marker.showInfoWindow();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "올바른 개수를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void onButton2Click(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("개수 입력(최대 2개만 가져갈 수 있습니다)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputValue = input.getText().toString();
                try {
                    int intValue = Integer.parseInt(inputValue);

                    if (intValue <= 2 && n[tmp] - intValue >= 0) {
                        n[tmp] -= intValue;
                        Toast.makeText(MainActivity.this, "뺀 개수: " + intValue, Toast.LENGTH_SHORT).show();

                        // 기존 마커의 snippet 업데이트
                        Marker marker = markerList.get(tmp);
                        if (n[tmp] > 9) {
                            marker.setSnippet("10개 이상 남았습니다.");
                        } else {
                            marker.setSnippet(n[tmp] + "개 남았습니다.");
                        }
                        marker.showInfoWindow();
                    } else if (intValue > 2) {
                        Toast.makeText(MainActivity.this, "최대 2개만 가져갈 수 있습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "현재 개수가 부족합니다. 죄송합니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "올바른 개수를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void onAddButtonClick(View view) {
        // 새로운 마커의 위치
        LatLng newMarkerLocation = new LatLng(current_latitude, current_hardness);
        for (Marker marker : markerList){
            if (marker.getPosition().latitude==current_latitude&&marker.getPosition().longitude==current_hardness){
                Toast.makeText(MainActivity.this, "현재 위치에 우산통이 이미 존재합니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // AlertDialog를 이용하여 사용자로부터 문자열 입력받기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("현재 위치에 통 놓기 (위치를 알려주세요!)");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleInput = input.getText().toString();


                // 새로운 마커 추가
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(newMarkerLocation)
                        .snippet("0개 남았습니다.")
                        .title(titleInput) // 사용자가 입력한 문자열로 타이틀 설정
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("custom_marker", 115, 115))));

                // 마커 리스트에 추가
                markerList.add(newMarker);

                // n 배열 크기 늘리기
                int[] newN = new int[n.length + 1];
                System.arraycopy(n, 0, newN, 0, n.length);
                n = newN;

                // 새로운 마커에 대한 인덱스 할당
                int newIndex = markerList.size() - 1;

                // 카메라 이동
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newMarkerLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newMarkerLocation, 12));

                Toast.makeText(MainActivity.this, "현재 위치에 새로운 장소가 등록되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
