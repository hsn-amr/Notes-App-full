package com.example.notesapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RVNotes(private var notes: ArrayList<Note>, private val context: Context):
    RecyclerView.Adapter<RVNotes.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    private val notesDBHelper by lazy { NotesDBHelper(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = notes[position]

        holder.itemView.apply {
            tvNote.text = note.text

            btnUpdate.setOnClickListener { updateNote(note) }
            btnDelete.setOnClickListener { deleteNote(note) }
        }
    }

    override fun getItemCount() = notes.size

    private fun update(){
        notes = notesDBHelper.retrieveAllNotes()
        notifyDataSetChanged()
    }

    private fun updateNote(note: Note){
        val et = EditText(context)
        et.setText(note.text)
        AlertDialog.Builder(context)
            .setTitle("Update Note")
            .setView(et)
            .setPositiveButton("Edit"){_,_ ->
                notesDBHelper.updateNote(et.text.toString(), note.text)
                update()
            }
            .setNegativeButton("Cancel"){face,_ -> face.cancel()}
            .create()
            .show()
    }

    private fun deleteNote(note: Note){
        AlertDialog.Builder(context)
            .setTitle("Delete Note")
            .setMessage("Do You Want To Delete This Note?")
            .setPositiveButton("Yes"){_,_ ->
                notesDBHelper.deleteNote(note)
                update()
            }
            .setNegativeButton("No"){face,_ -> face.cancel()}
            .create()
            .show()
    }
}