package com.anhlt.maddiscover.fragments.event;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.adapter.image.ImageListAdapter;
import com.anhlt.maddiscover.adapter.ReportListAdapter;
import com.anhlt.maddiscover.entities.EventImage;
import com.anhlt.maddiscover.entities.Report;
import com.anhlt.maddiscover.form.EventForm;
import com.anhlt.maddiscover.fragments.BaseFragment;
import com.anhlt.maddiscover.services.BaseService;
import com.anhlt.maddiscover.services.EventImagesService;
import com.anhlt.maddiscover.services.EventService;
import com.anhlt.maddiscover.services.ReportService;
import com.anhlt.maddiscover.utils.ExpandableGridView;
import com.squareup.picasso.Picasso;

import android.view.View.MeasureSpec;
import android.widget.AbsListView.LayoutParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by anhlt on 3/13/16.
 */
public class EventDetails extends BaseFragment {

    static Long eventId;
    Context context;
    EventService eventService;
    TextView eventName,startDate,organizer,venue,remark,time;
    BaseService baseService;
    ReportService reportService;
    ReportListAdapter adapter;
    ListView list;
    ExpandableGridView gridView;
    EventImagesService eventImagesService;

    public static EventDetails instance;

    public static String[] eatFoodyImages = {};

    public static EventDetails getInstance(Long eventId){
        if(instance==null)
            EventDetails.eventId = eventId;
            instance =  new EventDetails();
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getBaseService().replaceFragment(ListEvent.getInstance());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            eventId = getArguments().getLong("eventId");
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        eventService = new EventService(context);
        reportService = new ReportService(context);
        adapter = new ReportListAdapter(context,reportService.getReports(eventId));
        eventImagesService = new EventImagesService(context);
        return inflater.inflate(R.layout.event_details, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_event_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == R.id.edit_event_detail){
            eventService.editEvent(getFragmentManager(),context, eventId);
        }else if (item.getItemId()  == R.id.delete_event_detail){
            deleteEvent(eventId);
        }else if(item.getItemId() == R.id.report_detail){
            showDialogReport();
        }else if(item.getItemId() == R.id.images){
            selectImage();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        init();
        super.onStart();
    }

    private void init(){
        getUI();
        intiData();
    }

    private void getUI(){
        eventName = (TextView)getView().findViewById(R.id.event_name);
        startDate = (TextView)getView().findViewById(R.id.txt_start_date);
        organizer = (TextView)getView().findViewById(R.id.organ);
        venue = (TextView)getView().findViewById(R.id.location);
        remark = (TextView)getView().findViewById(R.id.re);
        time = (TextView)getView().findViewById(R.id.start_time);
    }

    private void intiData(){
        if(eventId>0){
            EventForm event = eventService.getEventInfo(eventId);
            eventName.setText(event.getEventName());
            startDate.setText(event.getStartDate());
            organizer.setText(event.getOrganizer());
            venue.setText(event.getVenue());
            remark.setText(event.getRemark());
            time.setText(event.getDateOfEvent());
            list = (ListView) getView().getRootView().findViewById(R.id.list_report);
            list.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list);
        }
        eatFoodyImages = loadImageData();
        loadImage();

    }

    private void deleteEvent(final Long eventId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Delete Event");
        builder.setMessage("Do you want to delete event: " + eventName.getText());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventService.deleteEventId(eventId);
                baseService = getBaseService();
                baseService.replaceFragment(new ListEvent());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showDialogReport(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.report);
        dialog.setTitle("Add Report");

        final EditText reportTxt = (EditText) dialog.findViewById(R.id.report_value);
        Button add = (Button)dialog.findViewById(R.id.add_report);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_report);
        final TextView valid = (TextView) dialog.findViewById(R.id.validator);

        reportTxt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (reportTxt.getText().length() > 0)
                    valid.setText("");
            }

        });

        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String reportValue = reportTxt.getText().toString();
                                       if (reportValue.length() == 0)
                                           valid.setText("Please enter report!");
                                       else {
                                           Report report = new Report();

                                           report.setCreateDate(new Date());
                                           report.setDetail(reportValue);
                                           report.setEventId(eventId);
                                           report.setTitle("");

                                           reportService.create(report);
                                           adapter = new ReportListAdapter(context, reportService.getReports(eventId));
                                           adapter.notifyDataSetChanged();
                                           list.setAdapter(adapter);
                                           setListViewHeightBasedOnChildren(list);
                                           dialog.cancel();
                                       }
                                   }
                               }
        );

        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          dialog.cancel();
                                      }
                                  }
        );

        dialog.show();

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void loadImage(){

        WindowManager wm = (WindowManager)getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gridView = (ExpandableGridView)getView().findViewById(R.id.event_image);
        gridView.setNumColumns(size.x/100 -1);
        gridView.setExpanded(true);
        gridView.setAdapter(new ImageListAdapter(getActivity(), eatFoodyImages));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), ImageActivity.class);
//                intent.putExtra("position", position);
//                startActivity(intent);

                Dialog imageDialog = new Dialog(getActivity());
                imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                imageDialog.setContentView(R.layout.image);

                final ImageView image = (ImageView) imageDialog.findViewById(R.id.image_view);
                Picasso.with(context).load(eatFoodyImages[position]).into(image);

                imageDialog.show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Configuration.REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), Configuration.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        }).setCancelable(true);
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            String imageUrl = data.getData().toString();

            if (requestCode == Configuration.REQUEST_CAMERA) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == Configuration.SELECT_FILE) {

            }
            saveImage(eventId,imageUrl);
            eatFoodyImages = loadImageData();
            gridView.setAdapter(new ImageListAdapter(getActivity(), eatFoodyImages));
        }
    }

    private void saveImage(Long eventId, String imageUrl){
        EventImage image = new EventImage();
        image.setFileName(imageUrl);
        image.setEventId(eventId);
        eventImagesService.create(image);
    }

    private String[] loadImageData(){
        return eventImagesService.eventImage(eventId);
    }

    private BaseService getBaseService(){
        if(baseService == null)
            baseService = new BaseService(getActivity().getFragmentManager(), context);
        return baseService;
    }

}
