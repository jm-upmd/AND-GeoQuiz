package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;


public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity"; // Etiqueta para los Log.

    // Clave para gurardar en el Bundle el indice de la última pregunta utilizada
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    // View que mostrára la pregunta
    private TextView mQuestionTextView;

    // Array de objetos Question. Llama al constructuor pasándole el identificador R.string


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        // Extraigo el número de indice que se guardó a través del
        // onSaveInstanceState(...)
        // Esto se puede hacer también en onRestoreInstanceState

        if (savedInstanceState != null) { // Si es la primera vez que se ejecuta es null
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // Creamos referencias a los distintos wiews que contiene el layout de la actividad

        mQuestionTextView =  findViewById(R.id.question_text_view);
        Button mTrueButton =  findViewById(R.id.true_button);
        Button mFalseButton =  findViewById(R.id.false_button);
        Button mNextButton =  findViewById(R.id.next_button);
        Button mCheatButton =  findViewById(R.id.cheat_button);

        // Dotamos de listeners a los views para que respondan a las acciones que realicemos
        // sobre ellos.

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });


        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                // Si estoy dentro de una clase anónima para hacer referencia a la clase contenedora
                // tengo que hacerlo como NombreClaseContenedora.this

                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "*****onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
       
        mQuestionTextView.setText(question); // Método sobrecargado tb. admite id del recurso texto
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId ;

        if (mIsCheater) { // Si consulto respuesta en la CheatActivity
            messageResId = R.string.judgment_toast;
        } else { // Si no lo hizo entonces comprueba si el boton pulsado corresponde con la respuesta correcta
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }
}
