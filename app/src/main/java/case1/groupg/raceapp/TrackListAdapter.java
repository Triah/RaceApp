package case1.groupg.raceapp;

import android.content.Intent;
import android.graphics.Typeface;
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
        ImageView fromToLengthSubtitution;
        TextView endPlace;
        ImageView trackIcon;
        Typeface raceFont;

        public TrackListViewHolder(View itemView) {
            super(itemView);

//            Typeface raceFont = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Regular.ttf");

            startPlace = itemView.findViewById(R.id.start_place); //startPlace.setTypeface(raceFont);
            trackLength = itemView.findViewById(R.id.track_length); //trackLength.setTypeface(raceFont);
            fromToLengthSubtitution = itemView.findViewById(R.id.from_to);
            endPlace = itemView.findViewById(R.id.end_place); //endPlace.setTypeface(raceFont);
            trackIcon = itemView.findViewById(R.id.track_icon);
            raceFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/PassionOne-Regular.ttf");
        }

        public void bindView(final int position, final ArrayList<Track> tracks){
            startPlace.setText(String.valueOf(tracks.get(position).getStartAddress())); startPlace.setTypeface(raceFont);
            trackLength.setText(String.valueOf(tracks.get(position).getLength())); trackLength.setTypeface(raceFont);
            fromToLengthSubtitution.setImageResource(R.drawable.right_arrow);
            endPlace.setText(String.valueOf(tracks.get(position).getEndAddress())); endPlace.setTypeface(raceFont);
            trackIcon.setImageResource(R.drawable.flag);
            trackIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent createRoute = new Intent(view.getContext(), MainActivity.class);
                    createRoute.putExtra("startLat", tracks.get(position).getLatitudeStart());
                    createRoute.putExtra("startLng", tracks.get(position).getLongitudeStart());
                    createRoute.putExtra("endLat", tracks.get(position).getLatitudeEnd());
                    createRoute.putExtra("endLng", tracks.get(position).getLongitudeEnd());
                    view.getContext().startActivity(createRoute);
                }
            });
        }

        @Override
        public void onClick(View view) { // Starts the route calculation for a track chosen from the list

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row, parent, false);
        return new TrackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TrackListViewHolder) holder).bindView(position, DefineRouteActivity.tracks);
    }

    @Override
    public int getItemCount() {
//        DefineRouteActivity.tracks = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            DefineRouteActivity.tracks.add(new Track(1, 2, 3, 4, 5, null));
//        }

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + DefineRouteActivity.tracks.size());
        return DefineRouteActivity.tracks.size();
    }
}
