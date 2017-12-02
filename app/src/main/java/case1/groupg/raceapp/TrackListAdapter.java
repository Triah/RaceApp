package case1.groupg.raceapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by barneygarda on 2017. 12. 02..
 */

public class TrackListAdapter extends RecyclerView.Adapter {

    private class TrackListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startPlace;
        TextView trackLength;
        TextView endPlace;
        ImageView trackIcon;

        public TrackListViewHolder(View itemView) {
            super(itemView);

//            Typeface raceFont = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Regular.ttf");

            startPlace = itemView.findViewById(R.id.start_place); //startPlace.setTypeface(raceFont);
            trackLength = itemView.findViewById(R.id.track_length); //trackLength.setTypeface(raceFont);
            endPlace = itemView.findViewById(R.id.end_place); //endPlace.setTypeface(raceFont);
            trackIcon = itemView.findViewById(R.id.track_icon);
        }

        public void bindView(int position, ArrayList<Track> tracks){
            startPlace.setText(String.valueOf(tracks.get(position).getLatitudeStart())); // TODO place names pls
            trackLength.setText(String.valueOf(tracks.get(position).getLength()));
            endPlace.setText(String.valueOf(tracks.get(position).getLatitudeEnd())); // TODO place names pls
            trackIcon.setImageResource(R.drawable.flag);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row, parent, false);
        return new TrackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TrackListViewHolder) holder).bindView(position, MainActivity.tracks);
    }

    @Override
    public int getItemCount() {
        MainActivity.tracks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            MainActivity.tracks.add(new Track(1, 2, 3, 4, 5, null));
        }

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + MainActivity.tracks.size());
        return MainActivity.tracks.size();
    }
}
