package com.pearov.emotionscalendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class NoteForEditFragment extends Fragment {

    private static final String TAG = "NoteForEditFragment";

    private EditText titleEditText;
    private EditText noteEditText;
    private ImageButton acceptButton;
    private Note note = null;
    private String noteTitle;
    private DatabaseHelper db;
    private boolean createNewNote = false;

    private void fillBackGroundColours() {

        if (MainActivity.themeName.equals("Light")) {
            titleEditText.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeadMainLight));
            noteEditText.setBackgroundColor(getContext().getResources().getColor(R.color.colorMainLight));
            acceptButton.setImageResource(R.drawable.ic_tick_light);
            acceptButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeadMainLight));

        } else if(MainActivity.themeName.equals("Dark")) {
            titleEditText.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeadMainDark));
            noteEditText.setBackgroundColor(getContext().getResources().getColor(R.color.colorMainDark));
            acceptButton.setImageResource(R.drawable.ic_tick_dark);
            acceptButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeadMainDark));

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_note_layout, container, false);
        db = new DatabaseHelper(getActivity());

        titleEditText = view.findViewById(R.id.noteTitleEditText);
        noteEditText = view.findViewById(R.id.noteEditText);
        acceptButton = view.findViewById(R.id.acceptButton);
        Log.d(TAG, "onCreateView for editing a note has started.");
        titleEditText.setText("New note");
        noteEditText.setText("");


        noteTitle = NotesForDayActivity.getNoteTitle();
        if(!noteTitle.isEmpty()) {
            List list  = db.getAllNotesForDay(NotesForDayActivity.getRememberDay(), NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
            for (int i = 0; i < list.size(); i++) {
                if ( ((Note)list.get(i)).getTitle().equals(noteTitle))
                    note = ((Note)list.get(i));
            }
        }

        if (note != null) {
            titleEditText.setText(note.getTitle());
            noteEditText.setText(note.getNoteText());
        } else {
            titleEditText.setText("New note");
        }

        acceptButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                acceptButtonOnClick();
            }
        });

        fillBackGroundColours();
        return view;
    }

    public void fillData() {
        noteTitle = NotesForDayActivity.getNoteTitle();
        titleEditText.setText(NotesForDayActivity.getNoteTitle());
        noteEditText.setText(NotesForDayActivity.getNoteText());
    }

    public boolean isCreateNewNote() {
        return createNewNote;
    }

    public void setCreateNewNote(boolean createNewNote) {
        this.createNewNote = createNewNote;
    }

    // Created outside of the onClick method because the user may save the note while leaving (popup)
    private void acceptButtonOnClick() {

        // If accept is clicked we are going to go back to the other fragment
        ((NotesForDayActivity) getActivity()).setGoBackToFragment(false);

        CalendarDate date = db.getCalendarDateByDate(NotesForDayActivity.getRememberDay(),
                NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
        if (date == null) {
            date = new CalendarDate(NotesForDayActivity.getRememberDay(),
                    NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear(), "Default", 0, 0 );
            date = CalendarActivity.fillDate(date);
            db.addCalendarDate(date);
        }

        String title = titleEditText.getText().toString();
        String text = noteEditText.getText().toString();

        if (text.length() > 2000) {
            Toast.makeText(getActivity(), "Maximum length (2000) exceeded by: " + (2000-text.length()), Toast.LENGTH_LONG).show();
            // We didn't finish the note.
            ((NotesForDayActivity) getActivity()).setGoBackToFragment(true);
            NotesForDayActivity.setCreateNewNoteFlag(false);
            return;
        } else if (text.length() == 0) {
            Toast.makeText(getActivity(), "To save a note you need to enter some text.", Toast.LENGTH_SHORT).show();
            // We didn't finish the note.
            ((NotesForDayActivity) getActivity()).setGoBackToFragment(true);
            NotesForDayActivity.setCreateNewNoteFlag(false);
            return;
        }

        if(!title.isEmpty() && !text.isEmpty()) {

            // If it is empty we are creating a new note
            Note tempNote = null;
            if (createNewNote) {
                noteTitle = title;

                // Check if title matches other note titles.
                if(!noteTitle.isEmpty()) {
                    List list  = db.getAllNotesForDay(NotesForDayActivity.getRememberDay(), NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
                    for (int i = 0; i < list.size(); i++) {
                        if (((Note)list.get(i)).getTitle().equals(noteTitle)) {
                            tempNote = ((Note) list.get(i));
                            break;
                        }
                    }
                    if (tempNote != null) {
                        Toast.makeText(getContext(), "There is a note with that title already.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                List<Note> tempList = db.getAllNotesForDay(NotesForDayActivity.getRememberDay(), NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
                int noteId = tempList.size();
                for (Note t: tempList) {
                    if (t.getId() > noteId)
                        noteId = t.getId();
                }
                noteId += 1;

                Note temp = new Note(noteId, title, text);
                date.addNoteIdToString(noteId);
                String notesString = date.getNoteIdListString();
                boolean res = db.addNote(temp);
                if (res) {
                    if (!db.updateCalendarDateNotes(date, notesString)) {
                        db.deleteNoteById(noteId);
                        Toast.makeText(getContext(), "Something went wrong trying to update the note.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), "Error note with such name already exists", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getActivity(), "Note saved.", Toast.LENGTH_SHORT).show();
                ((NotesForDayActivity) getActivity()).refreshNotes();
                ((NotesForDayActivity) getActivity()).refreshNoteListAdapter();
                NotesForDayActivity.setCreateNewNoteFlag(false);
            } // If it isn't empty we are updating an old note
            else {
                Note note = db.getNoteIdByTitleAndDay(noteTitle, NotesForDayActivity.getRememberDay(), NotesForDayActivity.getRememberMonth(), NotesForDayActivity.getRememberYear());
                db.updateNote(note, new Note(note.getId(), title, text));
                noteTitle = title;
                Toast.makeText(getActivity(), "Note updated.", Toast.LENGTH_SHORT).show();
                ((NotesForDayActivity) getActivity()).refreshNotes();
                ((NotesForDayActivity) getActivity()).refreshNoteListAdapter();
                NotesForDayActivity.setCreateNewNoteFlag(false);
            }
        } else {
            ((NotesForDayActivity) getActivity()).setGoBackToFragment(true);

            if (title.isEmpty())
                Toast.makeText(getActivity(), "You haven't entered a title yet!", Toast.LENGTH_SHORT).show();
            if (text.isEmpty())
                Toast.makeText(getActivity(), "You haven't entered any text yet!", Toast.LENGTH_SHORT).show();
        }
    }
}
