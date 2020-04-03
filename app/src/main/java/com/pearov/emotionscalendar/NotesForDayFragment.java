package com.pearov.emotionscalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotesForDayFragment extends Fragment {

    private RelativeLayout allNotesFragmentRelativeLayout;
    private TextView addNoteTextView;
    private ImageButton removeNoteImageButton;
    private ListView notesListView;
    private ArrayList<Note> notes;
    private ArrayList<String> noteTitles = new ArrayList<>();
    private ArrayList<String> noteTexts = new ArrayList<>();
    private DatabaseHelper db;
    private ArrayList<Integer> tickedForDeletionList = new ArrayList<>();
    private NotesListAdapter adapter;
    // When hold clicking an item you enter tickMode - select any item to be deleted and use the bin button
    private static boolean tickMode = false;


    private void fillBackGroundColours() {
        Context context = ((NotesForDayActivity) getActivity()).getContext();
        if (MainActivity.themeName.equals("Light")) {
            allNotesFragmentRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            addNoteTextView.setText("+ new");
            addNoteTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            addNoteTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            removeNoteImageButton.setImageResource(R.drawable.ic_white_trash);
            removeNoteImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
            allNotesFragmentRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            addNoteTextView.setText("+ new");
            addNoteTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            addNoteTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            removeNoteImageButton.setImageResource(R.drawable.ic_dark_trash);
            removeNoteImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));

        }
    }

    public void setupNotesListAdapter() {
        adapter.setupNotes();
    }

    private boolean checkIfElementInTickList(int element) {
        for (int i = 0; i < tickedForDeletionList.size(); i++) {
            if (tickedForDeletionList.get(i) == notes.get(element).getId())
                return true;
        }
        return false;
    }

    public static boolean isTickMode() {
        return tickMode;
    }

    public static void setTickMode(boolean mode) {
        tickMode = mode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_notes_layout, container, false);

        allNotesFragmentRelativeLayout = view.findViewById(R.id.AllNotesFragment);
        addNoteTextView = view.findViewById(R.id.addNoteTextView);
        removeNoteImageButton = view.findViewById(R.id.deleteNoteImageButton);
        notesListView = view.findViewById(R.id.notesListView);

        db = new DatabaseHelper(getActivity());

        notes = (ArrayList<Note>) db.getAllNotesForDay(NotesForDayActivity.getRememberDay(),
                NotesForDayActivity.getRememberMonth(),
                NotesForDayActivity.getRememberYear());

        for (Note note : notes) {
            noteTitles.add(note.getTitle());
            noteTexts.add(note.getNoteText());
        }

        adapter = new NotesListAdapter(((NotesForDayActivity) getActivity()).getContext());
        notesListView.setAdapter(adapter);

        // Moves to new Fragment to add a new note.
        addNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restrict creation to 10 notes.
                List<Note> list = ((NotesForDayActivity) getActivity()).getNotes();
                if (list.size() >= 10) {
                    Toast.makeText(getActivity(), "Cannot create more than 10 notes for a day", Toast.LENGTH_SHORT);
                    return;
                }

                ((NotesForDayActivity) getActivity()).setGoBackToFragment(true);
                NotesForDayActivity.setNoteTitle("New note");
                NotesForDayActivity.setNoteText("");
                NotesForDayActivity.setCreateNewNoteFlag(true);
                ((NotesForDayActivity)getActivity()).setViewPager(1);
            }
        });

        // Deletes all the market notes.
        removeNoteImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tickMode = false;
                List<Note> list = db.getAllNotesForDay(NotesForDayActivity.getRememberDay(), NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
                for(int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < tickedForDeletionList.size(); j++) {
                        if (tickedForDeletionList.get(j) == list.get(i).getId()) {
                            db.deleteNoteById(list.get(i).getId());
                        }
                    }
                }
                tickedForDeletionList.clear();
                ((NotesForDayActivity) getActivity()).refreshNotes();
            }
        });

        // If you have entered tickMode you check to see whether you have any elements, if not we add it
        // If we do have elements then we check if it is a element we have we delete it otherwise we add it.
        notesListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean shouldReturn = false;
                ImageView tickImage = view.findViewById(R.id.tickImageView);
                NotesForDayActivity.setNoteTitle(noteTitles.get(position));
                NotesForDayActivity.setNoteText(noteTexts.get(position));

                if (!tickMode) {
                    ((NotesForDayActivity) getActivity()).setGoBackToFragment(true);
                    ((NotesForDayActivity) getActivity()).setViewPager(1);
                } else {
                    if (tickedForDeletionList.size() == 0) {
//                        tickedForDeletionList.add(position);
                        tickedForDeletionList.add(((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId());
                        tickImage.setVisibility(View.VISIBLE);
                        return;
                    }
                    for (int i = 0; i < tickedForDeletionList.size(); i++) {
                        if (checkIfElementInTickList(position)) {
                            if (tickedForDeletionList.get(i) == ((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId()) {
                                tickedForDeletionList.remove(i);
                                if (tickImage != null) {
                                    tickImage.setVisibility(View.GONE);
                                }
                                shouldReturn = true;
                            }
                        }
                    }
                    if (shouldReturn)
                        return;
                    if (!checkIfElementInTickList(position)) {
                        if (tickImage != null) {
                            tickImage.setVisibility(View.VISIBLE);
                        }
                        tickedForDeletionList.add(((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId());
                    }
                    return;
                }
            }
        });

        // Same as other clicker but enters tickMode first.
        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                boolean shouldReturn = false;
                ImageView tickImage = view.findViewById(R.id.tickImageView);
                tickMode = true;

                if (tickedForDeletionList.size() == 0) {
                    tickedForDeletionList.add(((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId());
                    tickImage.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    if (tickedForDeletionList.size() == 0) {
                        tickedForDeletionList.add(((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId());
                        tickImage.setVisibility(View.VISIBLE);
                        return true;
                    }
                    for (int i = 0; i < tickedForDeletionList.size(); i++) {
                        if (checkIfElementInTickList(position)) {
                            if (tickedForDeletionList.get(i) == ((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId()) {
                                tickedForDeletionList.remove(i);
                                if (tickImage != null) {
                                    tickImage.setVisibility(View.GONE);
                                }
                                shouldReturn = true;
                            }
                        }
                    }
                    if (shouldReturn)
                        return true;
                    if (!checkIfElementInTickList(position)) {
                        if (tickImage != null) {
                            tickImage.setVisibility(View.VISIBLE);
                        }
                        tickedForDeletionList.add(((NotesListAdapter) notesListView.getAdapter()).getNotes().get(position).getId());
                    }
                    return true;
                }
            }
        });

        fillBackGroundColours();
        return view;
    }

}

