package com.picturebooks.mobilepicturebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

import pictureeditor.component.InputContentRepresentation;
import sentencegenerator.LASGenerator;
import sentencegenerator.ReferringExpressionGenerator;
import sentencegenerator.StoryGeneratorException;
import storyplanner.StoryPlannerException;
import storyplanner.component.StoryTree;
import storyplanner.introduction.IntroMaker;
import storyplanner.introduction.IntroMakerException;
import storyplanner.plot.PlotMaker;
import storyplanner.plot.ThemeExtractor;
import storyplanner.title.TitleMaker;
import storyplanner.title.TitleMakerException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.webkit.WebView;

import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import database.DatabaseHelper;
import database_entities.Background;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;
import database_entities.IGObject;
import database_entities.IGTheme;
import database_entities.SavedSticker;
import database_entities.StoryFile;

public class PictureEditor extends Activity {

	private static Context context;
	public static boolean createdStory = false;
	public static IGCharacter adultChar = new IGCharacter();
	private DatabaseHelper dbHelper;

	private int tutorialStep = 0;

	private String username;
	private int age;
	private int storyId;
	private int tutorialMode;
	private boolean loadStory;
	private int isUserAuthor;

	
	private ThemeExtractor themeExtractor = new ThemeExtractor();
	// private IGTheme chosenTheme = null;
	private StoryTree storyTree = null;

	// user selected
	private Background selectedBackground = null;
	private Vector<IGCharacter> charsInPic = new Vector<IGCharacter>();
	private Vector<IGCharacter> extrasInPic = new Vector<IGCharacter>();
	private Vector<IGObject> objectsInPic = new Vector<IGObject>();

	private Vector<String> story = new Vector<String>();
	private String generatedStory = new String();
	private String generatedTitle = new String();
	private String[] sentences;
	private String textDisplay;
	/*
	 * private Vector<StoryFile> storyFiles; private Vector<UserInformation>
	 * userInformation;
	 * 
	 * private Vector<Word> words; private Vector<Adjective> adjectives; private
	 * Vector<Adverb> adverbs; private Vector<Noun> nouns; private
	 * Vector<Article> articles; private Vector<Conjunction> conjunctions;
	 * private Vector<Verb> verbs; private Vector<Pronoun> pronouns; private
	 * Vector<Preposition> prepositions; private Vector<ConceptMapper>
	 * conceptMappers;
	 * 
	 * private Vector<Concept> concepts; private Vector<Ontology> ontology;
	 * 
	 * private Vector<IGObject> IGObjects; private Vector<IGCharacter>
	 * IGCharacters; private Vector<Background> backgrounds;
	 * 
	 * private Vector<StoryPlotTracker> storyPlotTracker; private
	 * Vector<CharacterGoal> characterGoals; private Vector<AuthorGoal>
	 * authorGoals; private Vector<IGTheme> IGThemes; private
	 * Vector<SemanticRelationRule> semanticRelationRules;
	 */

	GridView gridView;
	ImageView stickersBG;
	AbsoluteLayout pictureBackground;

	/*
	 * String[] Adults = new String[] { "adult_catmanimage",
	 * "adult_catwomanimage","adult_chickenmanimage", "adult_chickenwomanimage",
	 * "adult_dogmanimage", "adult_dogwomanimage", "adult_elephantmanimage",
	 * "adult_elephantwomanimage", "adult_giraffemanimage",
	 * "adult_giraffewomanimage", "adult_lionmanimage", "adult_lionwomanimage",
	 * "adult_pigmanimage", "adult_pigwomanimage", "adult_rabbitmanimage",
	 * "adult_rabbitwomanimage", "adult_sheepmanimage", "adult_sheepwomanimage",
	 * "adult_turtlemanimage", "adult_turtlewomanimage"};
	 * 
	 * String[] Kids = new String[] { "kid_catboyimage", "kid_catgirlimage",
	 * "kid_chickenboyimage", "kid_chickengirlimage", "kid_dogboyimage",
	 * "kid_doggirlimage", "kid_elephantboyimage", "kid_elephantgirlimage",
	 * "kid_giraffeboyimage", "kid_giraffegirlimage", "kid_lionboyimage",
	 * "kid_liongirlimage", "kid_pigboyimage", "kid_piggirlimage",
	 * "kid_rabbitboyimage", "kid_rabbitgirlimage", "kid_sheepboyimage",
	 * "kid_sheepgirlimage", "kid_turtleboyimage", "kid_turtlegirlimage"};
	 * 
	 * String[] Things = new String[] { "thing_alarm_clock", "thing_apple",
	 * "thing_backpack", "thing_bananas", "thing_beachball", "thing_book",
	 * "thing_bread", "thing_broccolis", "thing_brush", "thing_cake",
	 * "thing_candies", "thing_carrots", "thing_chair", "thing_doll",
	 * "thing_fried_chicken", "thing_glass_of_water", "thing_lamp",
	 * "thing_pillow", "thing_redball", "thing_rubber_ducky",
	 * "thing_salt_and_pepper", "thing_seesaw", "thing_soap", "thing_spaghetti",
	 * "thing_stethoscope", "thing_swingset", "thing_tea_set",
	 * "thing_television", "thing_thermometer", "thing_toothpaste_toothbrush",
	 * "thing_toy_blocks", "thing_toy_car", "thing_toy_horse",
	 * "thing_toy_truck", "thing_tricycle", "thing_wallet",
	 * "thing_weighing_scale"};
	 */

	private ArrayList<String> Adults;
	private ArrayList<String> Kids;
	private	ArrayList<String> Things;
	private ArrayList<String> Things_Bathroom = new ArrayList<String>();
	private ArrayList<String> Things_Bedroom = new ArrayList<String>();
	private ArrayList<String> Things_Classroom = new ArrayList<String>();
	private ArrayList<String> Things_Clinic = new ArrayList<String>();
	private ArrayList<String> Things_DiningRoom = new ArrayList<String>();
	private ArrayList<String> Things_Mall = new ArrayList<String>();
	private ArrayList<String> Things_Market = new ArrayList<String>();
	private ArrayList<String> Things_Outdoors = new ArrayList<String>();
	private ArrayList<String> Things_Playground = new ArrayList<String>();

	private ArrayList<String> SelectedAdults;
	private ArrayList<String> SelectedKids;
	private ArrayList<String> SelectedThings;

	RelativeLayout stickersLayout;
	RelativeLayout storyLayout;
	RelativeLayout bgTitleLayout;
	RelativeLayout definitionLayout;
	TextView storyTitle;
	TextView bgTitle;

	ImageView home_button;
	ImageView library_button;
	ImageView restart_button;
	ImageView createstory_button;
	ImageView editstory_button;

	ImageView right_button;
	ImageView left_button;

	ImageView adults_button;
	ImageView kids_button;
	ImageView things_button;

	ImageView storyTextViewBG;
	TextView storyTextView;
	TextView page;
	ImageView pageLeft_button;
	ImageView pageRight_button;

	ImageView dictionary_button;
	EditText search_bar;
	ImageView closedictionary_button;
	ListView dictionary_list;

	ImageView save_button;
	ImageView retry_button;
	ImageView searchdictionary_button;

	TextView wordTextView;
	TextView partOfSpeechTextView;
	TextView definitionTextView;

	private ArrayAdapter<String> adapter;

	private String background;
	private int backgroundID = 1;

	private int sentenceCount;
	private int currentPage, numberOfPages;
	private int sentenceLimitPerPage = 4;

	ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.picture_editor);
		dbHelper = new DatabaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		// InitializeDB();
		username = getIntent().getStringExtra("username");
		age = getIntent().getIntExtra("age", 6);
		tutorialMode = getIntent().getIntExtra("tutorial", 0);
		loadStory = getIntent().getBooleanExtra("loadStory", false);
		storyId = dbHelper.getMaxStoryId() + 1;

		stickersLayout = (RelativeLayout) findViewById(R.id.pe_stickersLayout);
		storyLayout = (RelativeLayout) findViewById(R.id.pe_storyLayout);
		bgTitleLayout = (RelativeLayout) findViewById(R.id.pe_bgTitleLayout);
		definitionLayout = (RelativeLayout) findViewById(R.id.pe_definitionLayout);
		storyTitle = (TextView) findViewById(R.id.pe_storytitle);
		bgTitle = (TextView) findViewById(R.id.pe_bgtitle);

		home_button = (ImageView) findViewById(R.id.pe_home_button);
		library_button = (ImageView) findViewById(R.id.pe_library_button);
		restart_button = (ImageView) findViewById(R.id.pe_restart_button);
		createstory_button = (ImageView) findViewById(R.id.pe_createstory_button);
		createstory_button.setEnabled(false);
		editstory_button = (ImageView) findViewById(R.id.pe_editstory_button);

		pictureBackground = (AbsoluteLayout) findViewById(R.id.pe_bg);
		stickersBG = (ImageView) findViewById(R.id.pe_stickers_bg);
		gridView = (GridView) findViewById(R.id.gridView1);

		right_button = (ImageView) findViewById(R.id.pe_right_button);
		left_button = (ImageView) findViewById(R.id.pe_left_button);

		adults_button = (ImageView) findViewById(R.id.pe_adults_button);
		kids_button = (ImageView) findViewById(R.id.pe_kids_button);
		things_button = (ImageView) findViewById(R.id.pe_things_button);

		storyTextViewBG = (ImageView) findViewById(R.id.pe_story_bg);
		storyTextView = (TextView) findViewById(R.id.pe_story);
		
		storyTextView.setWidth(storyTextViewBG.getWidth());
		storyTextView.setHeight(storyTextViewBG.getHeight());

		storyTextView.setMovementMethod(new ScrollingMovementMethod());
		page = (TextView) findViewById(R.id.pe_page);
		pageLeft_button = (ImageView) findViewById(R.id.pe_pageLeft_button);
		pageRight_button = (ImageView) findViewById(R.id.pe_pageRight_button);
		pageLeft_button.setEnabled(false);
		pageRight_button.setEnabled(false);

		dictionary_button = (ImageView) findViewById(R.id.pe_dictionary_button);
		search_bar = (EditText) findViewById(R.id.pe_searchbar);
		closedictionary_button = (ImageView) findViewById(R.id.pe_closedictionary_button);
		dictionary_list = (ListView) findViewById(R.id.pe_dictionary_list);
		save_button = (ImageView) findViewById(R.id.pe_savestory_button);
		retry_button = (ImageView) findViewById(R.id.pe_retrystory_button);
		searchdictionary_button = (ImageView) findViewById(R.id.pe_searchdictionary_button);

		wordTextView = (TextView) findViewById(R.id.pe_word);
		partOfSpeechTextView = (TextView) findViewById(R.id.pe_partofspeech);
		definitionTextView = (TextView) findViewById(R.id.pe_definition);
		definitionTextView.setMovementMethod(new ScrollingMovementMethod());
		SelectedAdults = new ArrayList<String>();
		SelectedKids = new ArrayList<String>();
		SelectedThings = new ArrayList<String>();

		Adults = new ArrayList<String>();
		Adults.add("adult_catmanimage_sam");
		Adults.add("adult_catwomanimage_sara");
		Adults.add("adult_chickenmanimage_robert");
		Adults.add("adult_chickenwomanimage_hannah");
		Adults.add("adult_dogmanimage_andre");
		Adults.add("adult_dogwomanimage_debbie");
		Adults.add("adult_elephantmanimage_andy");
		Adults.add("adult_elephantwomanimage_edna");
		Adults.add("adult_giraffemanimage_bill");
		Adults.add("adult_giraffewomanimage_gloria");
		Adults.add("adult_lionmanimage_lucas");
		Adults.add("adult_lionwomanimage_lalaine");
		Adults.add("adult_pigmanimage_philip");
		Adults.add("adult_pigwomanimage_patricia");
		Adults.add("adult_rabbitmanimage_owen");
		Adults.add("adult_rabbitwomanimage_francine");
		Adults.add("adult_sheepmanimage_gary");
		Adults.add("adult_sheepwomanimage_audrey");
		Adults.add("adult_turtlemanimage_nicolas");
		Adults.add("adult_turtlewomanimage_michelle");

		Kids = new ArrayList<String>();
		Kids.add("kid_catboyimage_calvin");
		Kids.add("kid_catgirlimage_cathy");
		Kids.add("kid_chickenboyimage_roy");
		Kids.add("kid_chickengirlimage_henny");
		Kids.add("kid_dogboyimage_daniel");
		Kids.add("kid_doggirlimage_denise");
		Kids.add("kid_elephantboyimage_edward");
		Kids.add("kid_elephantgirlimage_ellen");
		Kids.add("kid_giraffeboyimage_george");
		Kids.add("kid_giraffegirlimage_geena");
		Kids.add("kid_lionboyimage_leo");
		Kids.add("kid_liongirlimage_lenny");
		Kids.add("kid_pigboyimage_porky");
		Kids.add("kid_piggirlimage_pinky");
		Kids.add("kid_rabbitboyimage_robbie");
		Kids.add("kid_rabbitgirlimage_rizzy");
		Kids.add("kid_sheepboyimage_simon");
		Kids.add("kid_sheepgirlimage_sally");
		Kids.add("kid_turtleboyimage_toby");
		Kids.add("kid_turtlegirlimage_trixie");

		Things = new ArrayList<String>();
		/*
		 * Things.add("thing_alarm_clock"); Things.add("thing_apple");
		 * Things.add("thing_backpack"); Things.add("thing_bananas");
		 * Things.add("thing_beachball"); Things.add("thing_book");
		 * Things.add("thing_bread"); Things.add("thing_broccolis");
		 * Things.add("thing_brush"); Things.add("thing_cake");
		 * Things.add("thing_candies"); Things.add("thing_carrots");
		 * Things.add("thing_chair"); Things.add("thing_doll");
		 * Things.add("thing_fried_chicken");
		 * Things.add("thing_glass_of_water"); Things.add("thing_lamp");
		 * Things.add("thing_pillow"); Things.add("thing_ball");
		 * Things.add("thing_rubber_ducky");
		 * Things.add("thing_salt_and_pepper"); Things.add("thing_seesaw");
		 * Things.add("thing_soap"); Things.add("thing_spaghetti");
		 * Things.add("thing_stethoscope"); Things.add("thing_swing_set");
		 * Things.add("thing_tea_set"); Things.add("thing_television");
		 * Things.add("thing_thermometer");
		 * Things.add("thing_toothbrush_and_toothpaste");
		 * Things.add("thing_toy_blocks"); Things.add("thing_toy_car");
		 * Things.add("thing_toy_horse"); Things.add("thing_toy_truck");
		 * Things.add("thing_tricycle"); Things.add("thing_wallet");
		 * Things.add("thing_weighing_scale");
		 */
		Things_Bathroom.add("thing_rubber_ducky");
		Things_Bathroom.add("thing_soap");
		Things_Bathroom.add("thing_toothbrush_and_toothpaste");

		Things_Bedroom.add("thing_alarm_clock");
		Things_Bedroom.add("thing_doll");
		Things_Bedroom.add("thing_lamp");
		Things_Bedroom.add("thing_pillow");
		Things_Bedroom.add("thing_ball");
		Things_Bedroom.add("thing_tea_set");
		Things_Bedroom.add("thing_toy_blocks");
		Things_Bedroom.add("thing_toy_car");
		Things_Bedroom.add("thing_toy_horse");
		Things_Bedroom.add("thing_toy_truck");

		Things = Things_Bedroom;

		Things_Classroom.add("thing_backpack");
		Things_Classroom.add("thing_book");
		Things_Classroom.add("thing_doll");
		Things_Classroom.add("thing_ball");
		Things_Classroom.add("thing_toy_blocks");
		Things_Classroom.add("thing_toy_horse");

		Things_Clinic.add("thing_stethoscope");
		Things_Clinic.add("thing_thermometer");

		Things_DiningRoom.add("thing_apple");
		Things_DiningRoom.add("thing_bananas");
		Things_DiningRoom.add("thing_broccolis");
		Things_DiningRoom.add("thing_cake");
		Things_DiningRoom.add("thing_candies");
		Things_DiningRoom.add("thing_carrots");
		Things_DiningRoom.add("thing_glass_of_water");
		Things_DiningRoom.add("thing_spaghetti");
		Things_DiningRoom.add("thing_bread");

		Things_Mall.add("thing_beachball");
		Things_Mall.add("thing_doll");
		Things_Mall.add("thing_ball");
		Things_Mall.add("thing_tea_set");
		Things_Mall.add("thing_toy_car");
		Things_Mall.add("thing_toy_horse");
		Things_Mall.add("thing_toy_truck");
		Things_Mall.add("thing_tricycle");

		Things_Market.add("thing_cake");
		Things_Market.add("thing_candies");

		Things_Outdoors.add("thing_beachball");
		Things_Outdoors.add("thing_doll");
		Things_Outdoors.add("thing_ball");
		Things_Outdoors.add("thing_tea_set");
		Things_Outdoors.add("thing_toy_blocks");
		Things_Outdoors.add("thing_toy_car");
		Things_Outdoors.add("thing_toy_horse");
		Things_Outdoors.add("thing_toy_truck");

		Things_Playground.add("thing_doll");
		Things_Playground.add("thing_ball");
		Things_Playground.add("thing_swing_set");
		Things_Playground.add("thing_tea_set");
		Things_Playground.add("thing_toy_blocks");
		Things_Playground.add("thing_toy_car");
		Things_Playground.add("thing_toy_horse");
		Things_Playground.add("thing_toy_truck");
		Things_Playground.add("thing_tricycle");

		// Listview Data
		String dictionary_words[] = { "Dell Inspiron", "HTC One X",
				"HTC Wildfire S", "HTC Sense", "HTC Sensation XE", "iPhone 4S",
				"Samsung Galaxy Note 800", "Samsung Galaxy S3", "MacBook Air",
				"Mac Mini", "MacBook Pro" };

		Vector<database_entities.Word> words = dbHelper.getWords();
		dictionary_words = new String[words.size()];
		for (int a = 0; a < words.size(); a++) {
			dictionary_words[a] = words.get(a).getWord();
		}
		Arrays.sort(dictionary_words);

		dictionary_list = (ListView) findViewById(R.id.pe_dictionary_list);
		search_bar = (EditText) findViewById(R.id.pe_searchbar);

		// Adding items to listview
		adapter = new ArrayAdapter<String>(this, R.layout.dictionary_word,
				R.id.dictionary_word, dictionary_words);
		dictionary_list.setAdapter(adapter);
		dictionary_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String selectedWord = ((TextView) arg1
						.findViewById(R.id.dictionary_word)).getText()
						.toString();
				String partOfSpeech = dbHelper
						.findPartOfSpeechByWord(selectedWord);
				String definition = dbHelper.findDefinitionByWord(selectedWord);

				wordTextView.setText(selectedWord);
				partOfSpeechTextView.setText(partOfSpeech);
				definitionTextView.scrollTo(0, 0);
				definitionTextView.setText(Html.fromHtml(definition));
				definitionTextView
						.append("\n\n\nSource:\nKids Wordsmyth\n(http://kids.wordsmyth.net/)");
				dictionary_list.setVisibility(View.INVISIBLE);
				definitionLayout.setVisibility(View.VISIBLE);

				searchdictionary_button.setVisibility(View.VISIBLE);
				search_bar.setVisibility(View.INVISIBLE);
			}
		});

		gridView.setAdapter(new ImageAdapter(this, Adults));

		pictureBackground
				.setOnDragListener(new MyDragListener_RelativeLayout());
		gridView.setOnDragListener(new MyDragListener_GridView());

		home_button.setOnDragListener(new MyDragListener_GridView());
		library_button.setOnDragListener(new MyDragListener_GridView());
		restart_button.setOnDragListener(new MyDragListener_GridView());
		createstory_button.setOnDragListener(new MyDragListener_GridView());
		left_button.setOnDragListener(new MyDragListener_GridView());
		right_button.setOnDragListener(new MyDragListener_GridView());
		bgTitle.setOnDragListener(new MyDragListener_GridView());
		stickersLayout.setOnDragListener(new MyDragListener_GridView());

		restart_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pictureBackground.removeAllViews();
				for (int a = 0; a < SelectedAdults.size(); a++)
					Adults.add(SelectedAdults.remove(a));
				for (int a = 0; a < SelectedKids.size(); a++)
					Kids.add(SelectedKids.remove(a));
				for (int a = 0; a < SelectedThings.size(); a++)
					Things.add(SelectedThings.remove(a));

				Collections.sort(Adults);
				Collections.sort(Kids);
				Collections.sort(Things);

				gridView.setAdapter(new ImageAdapter(context, Adults));
				changeGridView(1);

				createstory_button
						.setImageResource(R.drawable.pe_createstory_button_disabled);
				createstory_button.setEnabled(false);
			}
		});

		searchdictionary_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchdictionary_button.setVisibility(View.INVISIBLE);
				search_bar.setVisibility(View.VISIBLE);
				dictionary_list.setVisibility(View.VISIBLE);
				definitionLayout.setVisibility(View.INVISIBLE);

			}
		});

		home_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent(PictureEditor.this,
						HomeActivity.class);
				PictureEditor.this.startActivity(mainIntent);
				PictureEditor.this.finish();
			}
		});

		library_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent(PictureEditor.this,
						LibraryActivity.class);
				PictureEditor.this.startActivity(mainIntent);
				PictureEditor.this.finish();
			}
		});

		save_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SaveAsync(PictureEditor.context).execute();
			}
		});

		retry_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new GetTaskRandom(PictureEditor.context).execute();
			}
		});

		createstory_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tutorialStep != 0) {
					tutorialNext(findViewById(R.id.tutorial_create_story));
				}
				new GetTask(PictureEditor.context).execute();

			}
		});

		pageRight_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPage < numberOfPages) {
					currentPage++;
					String textDisplay = new String();
					for (int a = (currentPage - 1) * sentenceLimitPerPage; a < currentPage
							* sentenceLimitPerPage
							&& a < sentenceCount; a++) {
						textDisplay += sentences[a] + ". ";
					}

					storyTextView.setText(textDisplay);
					storyTextView.scrollTo(0, 0);
					page.setText("Page " + currentPage + " of " + numberOfPages);
					if (currentPage >= numberOfPages) {
						pageRight_button
								.setImageResource(R.drawable.pe_right_button_disabled);
						pageRight_button.setEnabled(false);
					}
					pageLeft_button.setImageResource(R.drawable.pe_left_button);
					pageLeft_button.setEnabled(true);
				}
			}
		});

		pageLeft_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPage > 1) {
					currentPage--;
					String textDisplay = new String();
					for (int a = (currentPage - 1) * sentenceLimitPerPage; a < currentPage
							* sentenceLimitPerPage
							&& a < sentenceCount; a++) {
						textDisplay += sentences[a] + ". ";
					}

					storyTextView.setText(textDisplay);
					storyTextView.scrollTo(0, 0);
					page.setText("Page " + currentPage + " of " + numberOfPages);
					if (currentPage <= 1) {
						pageLeft_button
								.setImageResource(R.drawable.pe_left_button_disabled);
						pageLeft_button.setEnabled(false);
					}
					pageRight_button
							.setImageResource(R.drawable.pe_right_button);
					pageRight_button.setEnabled(true);
				}
			}
		});

		editstory_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				for (int i = 0; i < pictureBackground.getChildCount(); i++) {
					View view = pictureBackground.getChildAt(i);
					view.setEnabled(true);
					PictureEditor.createdStory = false;
				}

				restart_button.setImageResource(R.drawable.pe_restart_button);
				restart_button.setEnabled(true);
				createstory_button.setVisibility(View.VISIBLE);
				createstory_button.setEnabled(false);
				createstory_button.setImageResource(R.drawable.pe_createstory_button_disabled);
				editstory_button.setVisibility(View.INVISIBLE);

				bgTitleLayout.setVisibility(View.VISIBLE);
				storyTitle.setVisibility(View.INVISIBLE);

				stickersLayout.setVisibility(View.VISIBLE);
				storyLayout.setVisibility(View.INVISIBLE);

			}
		});

		left_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (backgroundID == 0)
					backgroundID = 9;
				backgroundID--;
				changeBackground(backgroundID);
				clearThings();
				changeGridView(1);
				createstory_button
						.setImageResource(R.drawable.pe_createstory_button_disabled);
				createstory_button.setEnabled(false);
			}
		});

		right_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backgroundID++;
				changeBackground(backgroundID);
				clearThings();
				changeGridView(1);
				createstory_button
						.setImageResource(R.drawable.pe_createstory_button_disabled);
				createstory_button.setEnabled(false);
			}
		});

		adults_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeGridView(1);
				if (tutorialStep != 0) {
					tutorialNext(findViewById(R.id.tutorial_adults_tab));
				}
			}
		});

		kids_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeGridView(2);
				if (tutorialStep != 0) {
					tutorialNext(findViewById(R.id.tutorial_kids_tab));
				}
			}
		});

		things_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeGridView(3);
				if (tutorialStep != 0) {
					tutorialNext(findViewById(R.id.tutorial_things_tab));
				}
			}
		});

		dictionary_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search_bar.setVisibility(View.VISIBLE);
				closedictionary_button.setVisibility(View.VISIBLE);
				dictionary_list.setVisibility(View.VISIBLE);

				save_button.setVisibility(View.INVISIBLE);
				retry_button.setVisibility(View.INVISIBLE);
				pageLeft_button.setVisibility(View.INVISIBLE);
				pageRight_button.setVisibility(View.INVISIBLE);

				storyTextView.setVisibility(View.INVISIBLE);
				page.setVisibility(View.INVISIBLE);
				if (searchdictionary_button.getVisibility() == View.VISIBLE) {
					searchdictionary_button.setVisibility(View.INVISIBLE);
					search_bar.setVisibility(View.VISIBLE);
					dictionary_list.setVisibility(View.VISIBLE);
					definitionLayout.setVisibility(View.INVISIBLE);
				}
			}
		});

		closedictionary_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search_bar.setVisibility(View.INVISIBLE);
				searchdictionary_button.setVisibility(View.INVISIBLE);
				definitionLayout.setVisibility(View.INVISIBLE);
				closedictionary_button.setVisibility(View.INVISIBLE);
				dictionary_list.setVisibility(View.INVISIBLE);

				save_button.setVisibility(View.VISIBLE);
				retry_button.setVisibility(View.VISIBLE);
				pageLeft_button.setVisibility(View.VISIBLE);
				pageRight_button.setVisibility(View.VISIBLE);

				storyTextView.setVisibility(View.VISIBLE);
				page.setVisibility(View.VISIBLE);
			}
		});

		search_bar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				PictureEditor.this.adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		// dbHelper = new DatabaseHelper(this);
		// InitializeDB();
		/*
		 * storyFiles = dbHelper.getStoryFiles(); userInformation =
		 * dbHelper.getUserInformation();
		 * 
		 * words = dbHelper.getWords(); verbs = dbHelper.getVerbs(); pronouns =
		 * dbHelper.getPronouns(); prepositions = dbHelper.getPrepositions();
		 * nouns = dbHelper.getNouns(); conjunctions =
		 * dbHelper.getConjunctions(); //conceptMappers =
		 * dbHelper.getConceptMappers(); articles = dbHelper.getArticles();
		 * adverbs = dbHelper.getAdverbs(); adjectives =
		 * dbHelper.getAdjectives();
		 * 
		 * concepts = dbHelper.getConcepts(); ontology =
		 * dbHelper.getOntologies();
		 * 
		 * IGCharacters = dbHelper.getIGCharacters(); IGObjects =
		 * dbHelper.getIGObjects(); backgrounds = dbHelper.getBackgrounds();
		 * 
		 * storyPlotTracker = dbHelper.getStoryPlotTracker(); characterGoals =
		 * dbHelper.getCharacterGoals(); authorGoals =
		 * dbHelper.getAuthorGoals(); IGThemes = dbHelper.getIGThemes();
		 * semanticRelationRules = dbHelper.getSemanticRelationRules();
		 */

		if (loadStory == true) {
			storyId = getIntent().getIntExtra("storyID", storyId);
			isUserAuthor = getIntent().getIntExtra("isUserAuthor", isUserAuthor); 
			Vector<SavedSticker> SavedStickers = dbHelper
					.getSavedStickers(storyId);
			StoryFile sf = dbHelper.findStoryFileById(storyId);
			String sfBackground = sf.getBackground();

			if (sfBackground.equals("bg_bathroom"))
				backgroundID = 0;
			else if (sfBackground.equals("bg_bedroom"))
				backgroundID = 1;
			else if (sfBackground.equals("bg_classroom"))
				backgroundID = 2;
			else if (sfBackground.equals("bg_clinic"))
				backgroundID = 3;
			else if (sfBackground.equals("bg_diningroom"))
				backgroundID = 4;
			else if (sfBackground.equals("bg_mall"))
				backgroundID = 5;
			else if (sfBackground.equals("bg_market"))
				backgroundID = 6;
			else if (sfBackground.equals("bg_outdoors"))
				backgroundID = 7;
			else if (sfBackground.equals("bg_playground"))
				backgroundID = 8;

			changeBackground(backgroundID);

			for (int i = 0; i < SavedStickers.size(); i++) {
				SavedSticker ss = SavedStickers.get(i);
				String ssType = ss.getType();
				String ssName = ss.getName();
				float ssX = ss.getX();
				float ssY = ss.getY();

				if (ssType.equals("adult")) {
					Adults.remove(ssName);
					SelectedAdults.add(ssName);
				} else if (ssType.equals("kid")) {
					Kids.remove(ssName);
					SelectedKids.add(ssName);
				} else if (ssType.equals("thing")) {
					Things.remove(ssName);
					SelectedThings.add(ssName);
				}

				int imageID = context.getResources().getIdentifier(ssName,
						"drawable", context.getPackageName());
				ImageView stickerImageView = new ImageView(this);
				stickerImageView.setImageDrawable(context.getResources()
						.getDrawable(imageID));
				stickerImageView.setContentDescription(ssName);
				stickerImageView.setX(ssX);
				stickerImageView.setY(ssY);
				pictureBackground.addView(stickerImageView);
			}

			gridView.setAdapter(new ImageAdapter(context, Adults));

			for (int i = 0; i < pictureBackground.getChildCount(); i++) {
				View view = pictureBackground.getChildAt(i);
				view.setEnabled(false);
			}

			generatedTitle = sf.getTitle();
			generatedStory = sf.getStory();

			sentences = generatedStory.split("\\.");
			sentenceCount = sentences.length;
			numberOfPages = (int) Math.ceil(sentenceCount
					/ sentenceLimitPerPage);
			if (numberOfPages == 0)
				numberOfPages = 1;
			currentPage = 1;

			for (int i = 0; i < pictureBackground.getChildCount(); i++) {
				View view = pictureBackground.getChildAt(i);
				view.setEnabled(false);
			}

			textDisplay = new String();

			for (int a = 0; a < sentenceLimitPerPage && a < sentenceCount; a++) {
				textDisplay += sentences[a] + ". ";
			}

			storyTitle.setText(generatedTitle);
			storyTextView.setText(textDisplay);
			storyTextView.scrollTo(0, 0);
			page.setText("Page " + currentPage + " of " + numberOfPages);
			if (numberOfPages > 1) {
				pageRight_button.setImageResource(R.drawable.pe_right_button);
				pageRight_button.setEnabled(true);
			}
			restart_button
					.setImageResource(R.drawable.pe_restart_button_disabled);
			restart_button.setEnabled(false);
			createstory_button.setVisibility(View.INVISIBLE);
			editstory_button.setVisibility(View.VISIBLE);

			bgTitleLayout.setVisibility(View.INVISIBLE);
			storyTitle.setVisibility(View.VISIBLE);

			stickersLayout.setVisibility(View.INVISIBLE);
			storyLayout.setVisibility(View.VISIBLE);
			
			if (isUserAuthor == 0) {

				editstory_button.setImageResource(R.drawable.pe_editstory_button_disabled);
				editstory_button.setEnabled(false);
				retry_button.setImageResource(R.drawable.pe_retrystory_button_disabled);
				retry_button.setEnabled(false);
				save_button.setImageResource(R.drawable.pe_savestory_button_disabled);
				save_button.setEnabled(false);
				
				
			}
		}
		if (tutorialMode == 1) {
			toggleEnableEvents(false);
			ImageView image = (ImageView) findViewById(R.id.tutorial_home);
			image.setVisibility(View.VISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_skip);
			image.setVisibility(View.VISIBLE);
		}
	}

	public void clearThings() {
		for (int a = 0; a < pictureBackground.getChildCount(); a++) {
			View view = pictureBackground.getChildAt(a);
			if (view.getContentDescription().toString().startsWith("thing_") == true)
				pictureBackground.removeViewAt(a);
		}
		if(SelectedThings.size() != 0)
			SelectedThings.remove(0);

		int ctr = 0;
		while(SelectedThings.size() != 0){
			Log.e("hello", SelectedThings.get(ctr).toString());
			SelectedThings.remove(ctr);
			ctr++;
		}
	}

	public void clearImage(int i) {
		int a = 0;
		while(i == 1 && SelectedKids.size() > 0){
			View view = pictureBackground.getChildAt(a);
			if (view.getContentDescription().toString().startsWith("kid_") == true){
				Kids.add(view.getContentDescription().toString());
				SelectedKids.remove(0);
				pictureBackground.removeViewAt(a);
			}
			a++;
		}
		while(i == 2 && SelectedAdults.size() > 0){
			View view = pictureBackground.getChildAt(a);
			if(view.getContentDescription().toString().startsWith("adult_") == true){
				Adults.add(view.getContentDescription().toString());
				SelectedAdults.remove(0);
				pictureBackground.removeViewAt(a);
			}
			a++;
		}
		while(i == 3 && SelectedThings.size() > 0){
			View view = pictureBackground.getChildAt(a);
			if(view.getContentDescription().toString().startsWith("thing_") == true){
				Things.add(view.getContentDescription().toString());
				SelectedThings.remove(0);
				pictureBackground.removeViewAt(a);
			}
			a++;
		}
	}

	public void changeBackground(int backgroundID) {

		switch (backgroundID % 9) {
		case 0:
			pictureBackground.setBackgroundResource(R.drawable.bg_bathroom);
			pictureBackground.setContentDescription("bg_bathroom");
			Things = Things_Bathroom;
			bgTitle.setText("Bathroom");
			break;
		case 1:
			pictureBackground.setBackgroundResource(R.drawable.bg_bedroom);
			pictureBackground.setContentDescription("bg_bedroom");
			Things = Things_Bedroom;
			bgTitle.setText("Bedroom");
			break;
		case 2:
			pictureBackground.setBackgroundResource(R.drawable.bg_classroom);
			pictureBackground.setContentDescription("bg_classroom");
			Things = Things_Classroom;
			bgTitle.setText("Classroom");
			break;
		case 3:
			pictureBackground.setBackgroundResource(R.drawable.bg_clinic);
			pictureBackground.setContentDescription("bg_clinic");
			Things = Things_Clinic;
			bgTitle.setText("Clinic");
			break;
		case 4:
			pictureBackground.setBackgroundResource(R.drawable.bg_diningroom);
			pictureBackground.setContentDescription("bg_diningroom");
			Things = Things_DiningRoom;
			bgTitle.setText("Dining Room");
			break;
		case 5:
			pictureBackground.setBackgroundResource(R.drawable.bg_mall);
			pictureBackground.setContentDescription("bg_mall");
			Things = Things_Mall;
			bgTitle.setText("Mall");
			break;
		case 6:
			pictureBackground.setBackgroundResource(R.drawable.bg_market);
			pictureBackground.setContentDescription("bg_market");
			Things = Things_Market;
			bgTitle.setText("Market");
			break;
		case 7:
			pictureBackground.setBackgroundResource(R.drawable.bg_outdoors);
			pictureBackground.setContentDescription("bg_outdoors");
			Things = Things_Outdoors;
			bgTitle.setText("Outdoors");
			break;
		case 8:
			pictureBackground.setBackgroundResource(R.drawable.bg_playground);
			pictureBackground.setContentDescription("bg_playground");
			Things = Things_Playground;
			bgTitle.setText("Playground");
			break;

		}
	}

	public void changeGridView(int choice) {
		switch (choice) {
		case 1:
			gridView.setAdapter(new ImageAdapter(this, Adults));
			stickersBG.setImageResource(R.drawable.pe_adults_bg);
			break;
		case 2:
			gridView.setAdapter(new ImageAdapter(this, Kids));
			stickersBG.setImageResource(R.drawable.pe_kids_bg);
			break;
		case 3:
			gridView.setAdapter(new ImageAdapter(this, Things));
			stickersBG.setImageResource(R.drawable.pe_things_bg);
			break;
		}
	}

	class MyDragListener_GridView implements OnDragListener {							// IMAGE CHOOSE AT THE RIGHT

		@Override
		public boolean onDrag(View v, DragEvent event) {

			int action = event.getAction();

			Log.d("MyCoordinates",
					"X: " + event.getX() + " - Y: " + event.getY());
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				View view = (View) event.getLocalState();
				view.playSoundEffect(SoundEffectConstants.CLICK);
				ViewGroup owner = (ViewGroup) view.getParent();
				View container = (View) v;
				if (owner.getContentDescription().equals(
						pictureBackground.getContentDescription()) == true) {
					owner.removeView(view);
					String contentDescription = view.getContentDescription()
							.toString();
					StringTokenizer strTok = new StringTokenizer(
							contentDescription, "_");
					String stickerCategory = strTok.nextToken();
					if (stickerCategory.equals("adult")) {
						System.out.println("ADULT: "+contentDescription);
						Adults.add(contentDescription);
						SelectedAdults.remove(contentDescription);
						changeGridView(1);
					} else if (stickerCategory.equals("kid")) {
						System.out.println("KID: "+contentDescription);
						Kids.add(contentDescription);
						SelectedKids.remove(contentDescription);
						changeGridView(2);
					} else if (stickerCategory.equals("thing")) {
						System.out.println("THING: "+contentDescription);
						Things.add(contentDescription);
						SelectedThings.remove(contentDescription);
						changeGridView(3);
					}
					if (SelectedKids.size() <= 0 || SelectedThings.size() <= 0) {
						createstory_button
								.setImageResource(R.drawable.pe_createstory_button_disabled);
						createstory_button.setEnabled(false);
					}
				}
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}

	class MyDragListener_RelativeLayout implements OnDragListener {								// PICTURE BACKGROUND EDITOR

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			Log.d("MyCoordinates", "MyCoordinates: X " + event.getX() + " - Y "
					+ event.getY());
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				ImageView view = (ImageView) event.getLocalState();
				view.playSoundEffect(SoundEffectConstants.CLICK);
				ViewGroup owner = (ViewGroup) view.getParent();
				ViewGroup container = (ViewGroup) v;
				Log.d("CONTAINER",
						"Container : " + container.getContentDescription());
				
				System.out.println("CHECK Container : " + container.getContentDescription());
				
				
				if (owner.getContentDescription().equals(container.getContentDescription()) == false) { 
					String contentDescription = view.getContentDescription().toString();
					StringTokenizer strTok = new StringTokenizer(contentDescription, "_");
					String stickerCategory = strTok.nextToken();
					
					if (stickerCategory.equals("adult")) {
						
						if(SelectedAdults.size() > 0)
							clearImage(2);
						
						Adults.remove(contentDescription);
						SelectedAdults.add(contentDescription);
						gridView.setAdapter(new ImageAdapter(context, Adults));
						
					} else if (stickerCategory.equals("kid")) {
						
						if(SelectedKids.size() > 0)
							clearImage(1);
						
						Kids.remove(contentDescription);
						SelectedKids.add(contentDescription);
						gridView.setAdapter(new ImageAdapter(context, Kids));
						
					} else if (stickerCategory.equals("thing")) {
						
						if(SelectedThings.size() > 0)
							clearImage(3);
						
						Things.remove(contentDescription);
						SelectedThings.add(contentDescription);
						gridView.setAdapter(new ImageAdapter(context, Things));
					}

					int imageID = context.getResources().getIdentifier(
							contentDescription, "drawable",
							context.getPackageName());
					BitmapDrawable bd = (BitmapDrawable) context.getResources()
							.getDrawable(imageID);
					int imageHeight = bd.getBitmap().getHeight();
					int imageWidth = bd.getBitmap().getWidth();

					view.getLayoutParams().height = imageHeight;
					view.getLayoutParams().width = imageWidth;

					Log.d("PictureCoordinates",
							"PictureCoordinates: X " + view.getX() + " - Y "
									+ view.getY());
					view.setX(event.getX() + view.getX() - 78);
					view.setY(event.getY() + view.getY() - 105);

					container.addView(view);
					if (SelectedKids.size() > 0 && SelectedThings.size() > 0 && SelectedAdults.size() > 0) {
						createstory_button
								.setImageResource(R.drawable.pe_createstory_button);
						createstory_button.setEnabled(true);
					}
				} else {
					view.setX(event.getX() - 80);
					view.setY(event.getY() - 100);
				}
				view.setVisibility(View.VISIBLE);
				if (tutorialStep != 0) {
					tutorialNext(findViewById(R.id.tutorial_stickers_hold));
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}

	private InputContentRepresentation createICR() {
		// TODO Auto-generated method stub
		InputContentRepresentation ICR = null;
		ICR = new InputContentRepresentation();
		ICR.setName(username);
		ICR.setAge(age);
		ICR.setBg(selectedBackground);
		ICR.setCharacters(charsInPic);
		ICR.setObjects(objectsInPic);
		return ICR;
	}

	class GetTask extends AsyncTask<Object, Void, String> {
		Context context;

		GetTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
/*
			mDialog = new ProgressDialog(context);
			mDialog.setMessage("Generating story...");
			mDialog.show();
			*/
            /*setContentView(R.layout.dialog);
           
            ImageView imageView=(ImageView) findViewById(R.id.imageView1);
           
            Animation a = AnimationUtils.loadAnimation(PictureEditor.this, R.layout.progress_anim);
            a.setDuration(2000);
            imageView.startAnimation(a);
           
            a.setInterpolator(new Interpolator()
            {
                private final int frameCount = 50;

                @Override
                public float getInterpolation(float input)
                {
                    return (float)Math.floor(input*frameCount)/frameCount;
                }
            });*/
			
			mDialog = new ProgressDialog(context);
			mDialog.setIndeterminate(true);
			mDialog.setIndeterminateDrawable(getResources().getDrawable(R.layout.progress_dialog_icon_drawable_animation));
			mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			//mDialog.setMessage("Some Text");
			mDialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			mDialog.show();
		}

		@Override
		protected String doInBackground(Object... params) {
			charsInPic.removeAllElements();
			objectsInPic.removeAllElements();
			
			//new
			

			for (int a = 0; a < SelectedAdults.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedAdults.get(a), "_");
				strTok.nextToken();
				strTok.nextToken();
				String characterName = strTok.nextToken();
				characterName = Character.toUpperCase(characterName.charAt(0))
						+ (String) characterName.subSequence(1,
								characterName.length());
				
				//new
			//	adultChar = new IGCharacter();
				adultChar = dbHelper.getCharacter(characterName);
				
				Log.d("PictureStickers", "SelectedAdult " + characterName);
			}
			for (int a = 0; a < SelectedKids.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedKids.get(a), "_");
				strTok.nextToken();
				strTok.nextToken();
				String characterName = strTok.nextToken();
				characterName = Character.toUpperCase(characterName.charAt(0))
						+ (String) characterName.subSequence(1,
								characterName.length());
				Log.d("PictureStickers", "SelectedAdult " + characterName);
				charsInPic.add(dbHelper.getCharacter(characterName));
			}
			for (int a = 0; a < SelectedThings.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedThings.get(a), "_");
				strTok.nextToken();
				String objectName = "";
				while (strTok.hasMoreTokens()) {
					objectName += strTok.nextToken() + " ";
				}
				objectName = (String) objectName.subSequence(0,
						objectName.length() - 1);
				Log.d("PictureStickers", "SelectedObject " + objectName);
				objectsInPic.add(dbHelper.getObject(objectName));
			}
			StringTokenizer strTok = new StringTokenizer(pictureBackground
					.getContentDescription().toString(), "_");
			strTok.nextToken();
			String backgroundName = strTok.nextToken();
			backgroundName = Character.toUpperCase(backgroundName.charAt(0))
					+ (String) backgroundName.subSequence(1,
							backgroundName.length());
			if (backgroundName.equals("Diningroom"))
				backgroundName = "DiningRoom";
			Log.d("PictureStickers", "SelectedBackground " + backgroundName);

			selectedBackground = dbHelper.getBackground(backgroundName);

			generatedStory = "";
			generatedTitle = "";

			InputContentRepresentation ICR = null;

			ICR = createICR();

			LASGenerator sg = new LASGenerator();
			PlotMaker pm = new PlotMaker(adultChar);
			
			boolean isError = false;

			try {
				IGTheme chosenTheme;
				chosenTheme = themeExtractor.determineTheme(ICR);
				System.out.println("Chosen theme: "
						+ chosenTheme.getMoralLesson() + " "
						+ chosenTheme.getID());

				pm.createStoryTree(chosenTheme, ICR);
				storyTree = pm.getStoryTree();
				System.out.println("\nplot maker completed successfully.\n");
			} catch (StoryPlannerException e) {
				isError = true;
				storyTree = pm.getStoryTree();
				System.out.println("Story Planner Module: Plot Maker Error.");
				e.printStackTrace();
			}

			try {
				new IntroMaker().createIntro(storyTree, age, ICR
						.getCharacters().get(0));
				System.out.println("\nintro maker completed successfully.\n");

			} catch (IntroMakerException e) {

				isError = true;
				System.out.println("Story Planner Module: Intro Maker Error.");
				e.printStackTrace();
			}

			try {
				new TitleMaker().createTitle(storyTree, age);
				System.out.println("\ntitle maker completed successfully.\n");

			} catch (TitleMakerException e) {
				isError = true;
				System.out.println("Story Planner Module: Title Maker Error.");
				e.printStackTrace();
			}

			Vector<CharacterGoal> storyLine = storyTree.getAllCharacterGoals();

			try {
				new ReferringExpressionGenerator().applyREG(storyLine,
						ICR.getAge());
				sg.setError(isError);

				System.out.println("START DEBUGGING PROBLEM (DB)>>>>");
				for (int i = 0; i < storyLine.size(); i++) {
					System.out.println("ID: " + storyLine.get(i).getID());
					System.out.println("ActionDB: "
							+ storyLine.get(i).getActionDB().getString());
					System.out.println("AgensDB: "
							+ storyLine.get(i).getAgensDB().getString());
					if (storyLine.get(i).getInstrumentDB() != null)
						System.out.println("InstrumenDBt: "
								+ storyLine.get(i).getInstrumentDB()
										.getString());
					if (storyLine.get(i).getPatiensDB() != null)
						System.out.println("PatiensDB: "
								+ storyLine.get(i).getPatiensDB().getString());
					System.out.println("String: "
							+ storyLine.get(i).getString());
					if (storyLine.get(i).getTargetDB() != null)
						System.out.println("TargetDB: "
								+ storyLine.get(i).getTargetDB().getString());
					System.out.println("END OF 1 STORYLINE");
				}

				System.out.println("START DEBUGGING PROBLEM (STRING)>>>>");
				for (int i = 0; i < storyLine.size(); i++) {
					System.out.println("ID: " + storyLine.get(i).getID());
					System.out.println("Action: "
							+ storyLine.get(i).getAction());
					System.out.println("Agens: " + storyLine.get(i).getAgens());
					if (storyLine.get(i).getInstrumentDB() != null)
						System.out.println("Instrument: "
								+ storyLine.get(i).getInstrument());
					if (storyLine.get(i).getPatiensDB() != null)
						System.out.println("Patiens: "
								+ storyLine.get(i).getPatiens());
					System.out.println("String: "
							+ storyLine.get(i).getString());
					if (storyLine.get(i).getTargetDB() != null)
						System.out.println("Target: "
								+ storyLine.get(i).getTarget());
					System.out.println("END OF 1 STORYLINE");
				}

				sg.generateSentence(storyLine);
				story = sg.getStory();

			} catch (StoryGeneratorException e) {

				isError = true;
				System.out.println("Story Generator Module Error.");
				e.printStackTrace();
			}

			if (isError) {
				System.out
						.println("There was a problem. The story may not be complete.");
				story = sg.getStory();
			}

			generatedTitle = story.elementAt(0);
			for (int i = 1; i < story.size(); i++) {
				generatedStory += story.elementAt(i);
			}

			Log.d("GENERATED STORY", "GENERATED STORY: " + generatedStory);
			sentences = generatedStory.split("\\.");
			sentenceCount = sentences.length;
			currentPage = 1;
			numberOfPages = (int) Math.ceil(sentenceCount
					/ sentenceLimitPerPage);
			if (numberOfPages == 0)
				numberOfPages = 1;

			textDisplay = new String();

			for (int a = 0; a < sentenceLimitPerPage && a < sentenceCount; a++) {
				textDisplay += sentences[a] + ". ";
			}

			return background;
		}

		@Override
		protected void onPostExecute(String result) {

			for (int i = 0; i < pictureBackground.getChildCount(); i++) {
				View view = pictureBackground.getChildAt(i);
				PictureEditor.createdStory = true;
			//	view.setEnabled(false);
			}

			storyTitle.setText(generatedTitle);
			storyTextView.setText(textDisplay);
			storyTextView.scrollTo(0, 0);
			page.setText("Page " + currentPage + " of " + numberOfPages);
			if (numberOfPages > 1) {
				pageRight_button.setImageResource(R.drawable.pe_right_button);
				pageRight_button.setEnabled(true);
			}
			restart_button
					.setImageResource(R.drawable.pe_restart_button_disabled);
			restart_button.setEnabled(false);
			createstory_button.setVisibility(View.INVISIBLE);
			editstory_button.setVisibility(View.VISIBLE);

			bgTitleLayout.setVisibility(View.INVISIBLE);
			storyTitle.setVisibility(View.VISIBLE);

			stickersLayout.setVisibility(View.INVISIBLE);
			storyLayout.setVisibility(View.VISIBLE);

			search_bar.setVisibility(View.INVISIBLE);
			searchdictionary_button.setVisibility(View.INVISIBLE);
			definitionLayout.setVisibility(View.INVISIBLE);
			closedictionary_button.setVisibility(View.INVISIBLE);
			dictionary_list.setVisibility(View.INVISIBLE);

			save_button.setVisibility(View.VISIBLE);
			retry_button.setVisibility(View.VISIBLE);
			pageLeft_button.setVisibility(View.VISIBLE);
			pageRight_button.setVisibility(View.VISIBLE);

			storyTextView.setVisibility(View.VISIBLE);
			page.setVisibility(View.VISIBLE);

			mDialog.dismiss();
		}

	}

	class GetTaskRandom extends AsyncTask<Object, Void, String> {
		Context context;

		GetTaskRandom(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog = new ProgressDialog(context);
			mDialog.setMessage("Generating story...");
			mDialog.show();
		}

		@Override
		protected String doInBackground(Object... params) {
			charsInPic.removeAllElements();
			objectsInPic.removeAllElements();
			
			//new
		//	IGCharacter adultChar = new IGCharacter();


			for (int a = 0; a < SelectedAdults.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedAdults.get(a), "_");
				strTok.nextToken();
				strTok.nextToken();
				String characterName = strTok.nextToken();
				characterName = Character.toUpperCase(characterName.charAt(0))
						+ (String) characterName.subSequence(1,
								characterName.length());
				
				//new
			//	adultChar = new IGCharacter();
				adultChar = dbHelper.getCharacter(characterName);
				
				Log.d("PictureStickers", "SelectedAdult " + characterName);
			}
			for (int a = 0; a < SelectedKids.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedKids.get(a), "_");
				strTok.nextToken();
				strTok.nextToken();
				String characterName = strTok.nextToken();
				characterName = Character.toUpperCase(characterName.charAt(0))
						+ (String) characterName.subSequence(1,
								characterName.length());
				Log.d("PictureStickers", "SelectedAdult " + characterName);
				charsInPic.add(dbHelper.getCharacter(characterName));
			}
			for (int a = 0; a < SelectedThings.size(); a++) {
				StringTokenizer strTok = new StringTokenizer(
						SelectedThings.get(a), "_");
				strTok.nextToken();
				String objectName = "";
				while (strTok.hasMoreTokens()) {
					objectName += strTok.nextToken() + " ";
				}
				objectName = (String) objectName.subSequence(0,
						objectName.length() - 1);
				Log.d("PictureStickers", "SelectedObject " + objectName);
				objectsInPic.add(dbHelper.getObject(objectName));
			}
			StringTokenizer strTok = new StringTokenizer(pictureBackground
					.getContentDescription().toString(), "_");
			strTok.nextToken();
			String backgroundName = strTok.nextToken();
			backgroundName = Character.toUpperCase(backgroundName.charAt(0))
					+ (String) backgroundName.subSequence(1,
							backgroundName.length());
			if (backgroundName.equals("Diningroom"))
				backgroundName = "DiningRoom";
			Log.d("PictureStickers", "SelectedBackground " + backgroundName);

			selectedBackground = dbHelper.getBackground(backgroundName);

			generatedStory = "";
			generatedTitle = "";

			InputContentRepresentation ICR = null;

			ICR = createICR();

			LASGenerator sg = new LASGenerator();
			PlotMaker pm = new PlotMaker(adultChar);
			
			boolean isError = false;

			try {
				IGTheme chosenTheme;
				chosenTheme = themeExtractor.determineThemeRandom(ICR);
				System.out.println("Chosen theme: "
						+ chosenTheme.getMoralLesson() + " "
						+ chosenTheme.getID());

				pm.createStoryTree(chosenTheme, ICR);
				storyTree = pm.getStoryTree();
				System.out.println("\nplot maker completed successfully.\n");
			} catch (StoryPlannerException e) {
				isError = true;
				storyTree = pm.getStoryTree();
				System.out.println("Story Planner Module: Plot Maker Error.");
				e.printStackTrace();
			}

			try {
				new IntroMaker().createIntro(storyTree, age, ICR
						.getCharacters().get(0));
				System.out.println("\nintro maker completed successfully.\n");

			} catch (IntroMakerException e) {

				isError = true;
				System.out.println("Story Planner Module: Intro Maker Error.");
				e.printStackTrace();
			}

			try {
				new TitleMaker().createTitle(storyTree, age);
				System.out.println("\ntitle maker completed successfully.\n");

			} catch (TitleMakerException e) {
				isError = true;
				System.out.println("Story Planner Module: Title Maker Error.");
				e.printStackTrace();
			}

			Vector<CharacterGoal> storyLine = storyTree.getAllCharacterGoals();

			try {
				new ReferringExpressionGenerator().applyREG(storyLine,
						ICR.getAge());
				sg.setError(isError);

				System.out.println("START DEBUGGING PROBLEM (DB)>>>>");
				for (int i = 0; i < storyLine.size(); i++) {
					System.out.println("ID: " + storyLine.get(i).getID());
					System.out.println("ActionDB: "
							+ storyLine.get(i).getActionDB().getString());
					System.out.println("AgensDB: "
							+ storyLine.get(i).getAgensDB().getString());
					if (storyLine.get(i).getInstrumentDB() != null)
						System.out.println("InstrumenDBt: "
								+ storyLine.get(i).getInstrumentDB()
										.getString());
					if (storyLine.get(i).getPatiensDB() != null)
						System.out.println("PatiensDB: "
								+ storyLine.get(i).getPatiensDB().getString());
					System.out.println("String: "
							+ storyLine.get(i).getString());
					if (storyLine.get(i).getTargetDB() != null)
						System.out.println("TargetDB: "
								+ storyLine.get(i).getTargetDB().getString());
					System.out.println("END OF 1 STORYLINE");
				}

				System.out.println("START DEBUGGING PROBLEM (STRING)>>>>");
				for (int i = 0; i < storyLine.size(); i++) {
					System.out.println("ID: " + storyLine.get(i).getID());
					System.out.println("Action: "
							+ storyLine.get(i).getAction());
					System.out.println("Agens: " + storyLine.get(i).getAgens());
					if (storyLine.get(i).getInstrumentDB() != null)
						System.out.println("Instrument: "
								+ storyLine.get(i).getInstrument());
					if (storyLine.get(i).getPatiensDB() != null)
						System.out.println("Patiens: "
								+ storyLine.get(i).getPatiens());
					System.out.println("String: "
							+ storyLine.get(i).getString());
					if (storyLine.get(i).getTargetDB() != null)
						System.out.println("Target: "
								+ storyLine.get(i).getTarget());
					System.out.println("END OF 1 STORYLINE");
				}

				sg.generateSentence(storyLine);
				story = sg.getStory();

			} catch (StoryGeneratorException e) {

				isError = true;
				System.out.println("Story Generator Module Error.");
				e.printStackTrace();
			}

			if (isError) {
				System.out
						.println("There was a problem. The story may not be complete.");
				story = sg.getStory();
			}

			generatedTitle = story.elementAt(0);
			for (int i = 1; i < story.size(); i++) {
				generatedStory += story.elementAt(i);
			}

			Log.d("GENERATED STORY", "GENERATED STORY: " + generatedStory);
			sentences = generatedStory.split("\\.");
			sentenceCount = sentences.length;
			currentPage = 1;
			numberOfPages = (int) Math.ceil(sentenceCount
					/ sentenceLimitPerPage);

			textDisplay = new String();

			for (int a = 0; a < sentenceLimitPerPage && a < sentenceCount; a++) {
				textDisplay += sentences[a] + ". ";
			}

			return background;
		}

		@Override
		protected void onPostExecute(String result) {

			for (int i = 0; i < pictureBackground.getChildCount(); i++) {
				View view = pictureBackground.getChildAt(i);
				PictureEditor.createdStory = true;
				//view.setEnabled(false);
			}

			storyTitle.setText(generatedTitle);
			storyTextView.setText(textDisplay);
			storyTextView.scrollTo(0, 0);
			page.setText("Page " + currentPage + " of " + numberOfPages);
			if (numberOfPages > 1) {
				pageRight_button.setImageResource(R.drawable.pe_right_button);
				pageRight_button.setEnabled(true);
			}
			restart_button
					.setImageResource(R.drawable.pe_restart_button_disabled);
			restart_button.setEnabled(false);
			createstory_button.setVisibility(View.INVISIBLE);
			editstory_button.setVisibility(View.VISIBLE);

			bgTitleLayout.setVisibility(View.INVISIBLE);
			storyTitle.setVisibility(View.VISIBLE);

			stickersLayout.setVisibility(View.INVISIBLE);
			storyLayout.setVisibility(View.VISIBLE);

			mDialog.dismiss();
		}

	}

	class SaveAsync extends AsyncTask<Object, Void, String> {
		Context context;

		SaveAsync(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog = new ProgressDialog(context);
			mDialog.setMessage("Saving story...");
			mDialog.show();
		}

		@Override
		protected String doInBackground(Object... params) {
			
			captureView(pictureBackground.getId(), username + "_" + storyId
					+ ".png");
			if (loadStory == false) {
				dbHelper.addStoryFile(new StoryFile(storyId, username,
						generatedTitle, generatedStory, pictureBackground
								.getContentDescription().toString()));
				Log.d("Log", "Log: Story has been added.");
			} else {
				dbHelper.updateStoryFile(new StoryFile(storyId, username,
						generatedTitle, generatedStory, pictureBackground
								.getContentDescription().toString()));
				Log.d("Log", "Log: Story has been updated.");
			}
			StoryFile sf = dbHelper.findStoryFileById(storyId);
			Vector<StoryFile> sfs = dbHelper.getStoryFilesById(username);
			for (int a = 0; a < sfs.size(); a++) {
				StoryFile temp = sfs.get(a);
				Log.d("STORYFILESSSS",
						"STORYFILESSSS" + temp.getStoryID() + " - "
								+ temp.getUsername() + " - "
								+ temp.getTitle() + temp.getBackground());
			}
			Log.d("STORYFILEADDED", "STORYFILEADDED" + sf.getStoryID()
					+ "\n" + sf.getUsername() + "\n" + sf.getTitle() + "\n"
					+ sf.getStory() + "\n" + sf.getBackground());

			dbHelper.deleteExistingStickers(storyId);
			for (int i = 0; i < pictureBackground.getChildCount(); i++) {
				View view = pictureBackground.getChildAt(i);
				String name = view.getContentDescription().toString();
				StringTokenizer strTok = new StringTokenizer(name, "_");
				String type = strTok.nextToken();
				dbHelper.addSavedSticker(new SavedSticker(storyId, type,
						name, view.getX(), view.getY()));
			}		
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mDialog.dismiss();

			Intent mainIntent = new Intent(PictureEditor.this,
					StoriesActivity.class);
			PictureEditor.this.startActivity(mainIntent);
			PictureEditor.this.finish();
			PictureEditor.createdStory = false;

		}
	}
	
	public void captureView(int viewId, String filename) {
		// Find the view we are after
		View view = (View) findViewById(viewId);
		// Create a Bitmap with the same dimensions
		Bitmap image = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.RGB_565);
		// Draw the view inside the Bitmap
		view.draw(new Canvas(image));

		// Store to sdcard
		try {
			String path = Environment.getExternalStorageDirectory().toString()
					+ "/MobilePictureBooks/SavedPictures";
			File myFile = new File(path);
			myFile.mkdirs();
			myFile = new File(path, filename);
			FileOutputStream out = new FileOutputStream(myFile);

			image.compress(Bitmap.CompressFormat.PNG, 90, out); // Output
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void tutorialNext(View v) {
		Log.d("Next Tutorial", "Next Tutorial");
		tutorialStep++;
		ImageView image;
		v.setVisibility(View.INVISIBLE);
		switch (tutorialStep) {
		case 0:
			image = (ImageView) findViewById(R.id.tutorial_home);
			image.setVisibility(View.VISIBLE);
			break;
		case 1:
			image = (ImageView) findViewById(R.id.tutorial_library);
			image.setVisibility(View.VISIBLE);
			break;
		case 2:
			image = (ImageView) findViewById(R.id.tutorial_delete);
			image.setVisibility(View.VISIBLE);
			break;
		case 3:
			image = (ImageView) findViewById(R.id.tutorial_right_bg);
			image.setVisibility(View.VISIBLE);
			right_button.setEnabled(true);
			break;
		case 4:
			image = (ImageView) findViewById(R.id.tutorial_left_bg);
			image.setVisibility(View.VISIBLE);
			left_button.setEnabled(true);
			break;
		case 5:
			image = (ImageView) findViewById(R.id.tutorial_requirements);
			image.setVisibility(View.VISIBLE);
			right_button.setEnabled(false);
			left_button.setEnabled(false);
			break;
		case 6:
			image = (ImageView) findViewById(R.id.tutorial_kids_tab);
			image.setVisibility(View.VISIBLE);
			kids_button.setEnabled(true);
			break;
		case 7:
			image = (ImageView) findViewById(R.id.tutorial_stickers_hold);
			image.setVisibility(View.VISIBLE);
			kids_button.setEnabled(false);
			toggleEnableGridView(true);
			pictureBackground.setEnabled(true);
			pictureBackground.setClickable(true);
			break;
		case 8:
			image = (ImageView) findViewById(R.id.tutorial_things_tab);
			image.setVisibility(View.VISIBLE);
			things_button.setEnabled(true);
			break;
		case 9:
			image = (ImageView) findViewById(R.id.tutorial_stickers_hold);
			image.setVisibility(View.VISIBLE);
			things_button.setEnabled(false);
			pictureBackground.setEnabled(true);
			pictureBackground.setClickable(true);
			break;
		case 10:
			image = (ImageView) findViewById(R.id.tutorial_adults_tab);
			image.setVisibility(View.VISIBLE);
			toggleEnableGridView(false);
			pictureBackground.setEnabled(false);
			pictureBackground.setClickable(false);
			adults_button.setEnabled(true);
			break;
		case 11:
			image = (ImageView) findViewById(R.id.tutorial_stickers_hold);
			image.setVisibility(View.VISIBLE);
			adults_button.setEnabled(false);
			pictureBackground.setEnabled(true);
			pictureBackground.setClickable(true);
			toggleEnableGridView(true);
			break;
		case 12:
			image = (ImageView) findViewById(R.id.tutorial_create_story);
			image.setVisibility(View.VISIBLE);
			createstory_button.setEnabled(true);
			break;
		default:
			image = (ImageView) findViewById(R.id.tutorial_home);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_library);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_delete);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_right_bg);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_left_bg);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_requirements);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_kids_tab);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_stickers_hold);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_things_tab);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_adults_tab);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_create_story);
			image.setVisibility(View.INVISIBLE);
			image = (ImageView) findViewById(R.id.tutorial_skip);
			image.setVisibility(View.INVISIBLE);
			toggleEnableEvents(true);
			break;
		}
	}

	public void tutorialSkip(View v) {
		Log.d("Skip Tutorial", "Skip Tutorial");
		v.setVisibility(View.INVISIBLE);
		tutorialStep = 12;
		tutorialNext(v);
	}

	public void toggleEnableEvents(boolean enabled) {
		toggleEnableGridView(enabled);
		home_button.setEnabled(enabled);
		library_button.setEnabled(enabled);
		restart_button.setEnabled(enabled);
		createstory_button.setEnabled(enabled);
		pictureBackground.setEnabled(enabled);
		left_button.setEnabled(enabled);
		right_button.setEnabled(enabled);
		adults_button.setEnabled(enabled);
		kids_button.setEnabled(enabled);
		things_button.setEnabled(enabled);
	}

	public void toggleEnableGridView(boolean enabled) {		
		gridView.setVerticalScrollBarEnabled(enabled);
		gridView.setEnabled(enabled);
		Log.d("OYEA", "OYEA " + enabled);
		for (int i = 0; i < gridView.getChildCount(); i++) {
			View view = gridView.getChildAt(i);
			view.setEnabled(enabled);
		}
		
	}

}
