package com.money.shmoney;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.service.media.MediaBrowserService.Result;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Typeface typeFace;
	RelativeLayout lay;
	int width = 0;
	int height = 0;
	TextView ghostplay, play, highscore, share, rateapp;
	TextView t1;
	TextView t2;
	TextView t3;
	ImageView i1;
	AlertDialog gameover;
	CountDownTimer stopleft, stopright;
	float staticmovevalue = 0;
	CountDownTimer breathing;
	int br = 0;
	ImageView background;
	SharedPreferences pref1;
	Random random = new Random();
	TextView ads;
	LayoutParams p, pan, g, l, r, a, b, st, hs, s, rat, gp;
	AlertDialog best;
	TextView scoretext;
	ImageView panel;
	ImageView guy;
	ArrayList<Integer> leftmargin;
	CountDownTimer makeitrain;
	CountDownTimer moveleft, moveright;
	int ml = 0;
	int mr = 0;
	Button left, right;
	int leftblock = 0;
	int rightblock = 0;
	int gmover = 0;
	TextView hscore;
	int score = 0;
	float speed;
	int millis = 700;
	int nextspeed = 0;
	Handler handler = new Handler();

	@Override
	protected void onResume() {
		mediaPlayer.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mediaPlayer.pause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	void setupHandler() {
		handler.removeCallbacks(run);
		handler.postDelayed(run, 10);
	}

	private Runnable run = new Runnable() {

		@Override
		public void run() {

			// code for spawning heads in time and speeding them up after reach certain score
			
			if (nextspeed >= 16) {
				nextspeed = 0;
				speed = speed + height / 400;
			}
			int randomleft = leftmargin.get(random.nextInt(leftmargin.size()));
			spawnHead(randomleft, speed);
			handler.postDelayed(run, millis);

		}
	};

	public void spawnHead(int leftt, final float s) {
		final ImageView obstacle = new ImageView(this);
		obstacle.setBackgroundResource(R.drawable.obstacle);
		final LayoutParams olp = new LayoutParams(width / 5,
				width * 1145 / 5000);
		olp.leftMargin = leftt;
		olp.topMargin = -width * 1145 / 5000;
		lay.addView(obstacle, olp);
		obstacle.setContentDescription("");
		CountDownTimer move = new CountDownTimer(6000, 30) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (obstacle.getContentDescription().equals("d")) {

				} else {
					if (olp.topMargin > (height - height / 6) - width * 1145
							/ 5000 - height / 8
							&& olp.topMargin < height) {
						obstacle.setBackgroundDrawable(null);
						obstacle.setContentDescription("d");

						if (gmover == 1) {

						} else {
							score++;
						}
						lay.removeView(obstacle);
						nextspeed++;
						SpannableString ss1 = new SpannableString("SCORE\n"
								+ score);
						ss1.setSpan(new RelativeSizeSpan(0.5f), 0, 5, 0);
						scoretext.setText(ss1);

					} else {
						if (gmover == 1) {
							obstacle.setBackgroundDrawable(null);
							obstacle.setContentDescription("d");
							lay.removeView(obstacle);
						} else {
							int x = g.leftMargin;
							int y = g.topMargin;
							olp.topMargin = (int) (olp.topMargin + s);
							lay.updateViewLayout(obstacle, olp);
							if (olp.leftMargin > x - width / 5 + width / 12
									&& olp.leftMargin < x + width / 4 - width
											/ 12
									&& olp.topMargin > y - width * 1145 / 5000
											+ height / 19
									&& olp.topMargin < y + height / 4) {
								gmover = 1;
								left.setEnabled(false);
								right.setEnabled(false);
								obstacle.setBackgroundDrawable(null);
								obstacle.setContentDescription("d");
								lay.removeView(obstacle);

								pref1 = getApplicationContext()
										.getSharedPreferences("highscore",
												MODE_PRIVATE);
								int bestscore = pref1.getInt("bestscore", 0);
								if (score > bestscore) {
									bestscore = score;
									Editor edit = pref1.edit();
									edit.putInt("bestscore", score);
									edit.commit();
									i1.setVisibility(View.VISIBLE);
								} else {
									i1.setVisibility(View.GONE);
								}

								t2.setText("SCORE: " + score);
								t3.setText("BEST: " + bestscore);

								gameover.show();
								handler.removeCallbacks(run);
								g.leftMargin = -200;
								lay.updateViewLayout(guy, g);
								moveleft.cancel();
								moveright.cancel();
								leftblock = 0;
								rightblock = 0;
								left.setBackgroundResource(R.drawable.leftmovebut);
								right.setBackgroundResource(R.drawable.rightmovebut);
								ml = 0;
								mr = 0;
								speed = (height) / 65.0f;
								millis = 700;
								nextspeed = 0;
								// reset all
							}
						}
					}
				}

			}

			@Override
			public void onFinish() {

			}
		};
		move.start();

	}

	MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		lay = (RelativeLayout) findViewById(R.id.lay);

		mediaPlayer = new MediaPlayer();
		mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.appsongtv);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;

		} else {
			Display display = getWindowManager().getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight();
		}

		stopleft = new CountDownTimer(10, 10) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				moveleft.cancel();

			}
		};

		stopright = new CountDownTimer(10, 10) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				moveright.cancel();

			}
		};

		staticmovevalue = width / 60.0f;

		speed = (height) / 65.0f;

		typeFace = Typeface.createFromAsset(getAssets(), "fonts/Army Thin.ttf");

		leftmargin = new ArrayList<Integer>(Arrays.asList(0, width / 5,
				width * 2 / 5, width * 3 / 5, width * 4 / 5));

		play = new TextView(this);
		play.setTypeface(typeFace);
		play.setTextColor(Color.parseColor("#fe1414"));
		play.setText("PLAY");
		play.setGravity(Gravity.CENTER);
		play.setTextSize(60);
		play.setShadowLayer((float) 0.01, -2, -2, Color.BLACK);
		p = new LayoutParams(width / 2, height * 2 / 15);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		p.topMargin = height / 11;

		ghostplay = new TextView(this);
		ghostplay.setBackgroundDrawable(null);
		ghostplay.setText("");
		gp = new LayoutParams(width / 2, height * 2 / 15);
		gp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		gp.topMargin = height / 11;

		ghostplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ghostplay.setEnabled(false);
				ghostplay.setVisibility(View.INVISIBLE);
				RotateAnimation rotate2 = new RotateAnimation(0, 90,
						Animation.RELATIVE_TO_PARENT, 0.5f,
						Animation.RELATIVE_TO_PARENT, 0.5f);
				rotate2.setDuration(2500);
				rotate2.setRepeatCount(0);

				play.setAnimation(rotate2);
				highscore.setAnimation(rotate2);
				rateapp.setAnimation(rotate2);
				share.setAnimation(rotate2);

				RotateAnimation rotate = new RotateAnimation(-90, 0,
						Animation.RELATIVE_TO_PARENT, 0.5f,
						Animation.RELATIVE_TO_PARENT, 0.5f);
				rotate.setDuration(2500);
				rotate.setRepeatCount(0);

				left.setAnimation(rotate);
				right.setAnimation(rotate);
				scoretext.setAnimation(rotate);

				left.setVisibility(View.VISIBLE);
				right.setVisibility(View.VISIBLE);
				scoretext.setVisibility(View.VISIBLE);
				left.setEnabled(false);
				right.setEnabled(false);
				scoretext.setEnabled(false);
				highscore.setEnabled(false);
				rateapp.setEnabled(false);
				share.setEnabled(false);

				breathing.cancel();

				CountDownTimer littlemove = new CountDownTimer(1500, 50) {

					@Override
					public void onTick(long millisUntilFinished) {
						if (mr == 0) {
							mr++;
							guy.setBackgroundResource(R.drawable.rightmove1);
							g.leftMargin = (int) (g.leftMargin + staticmovevalue);
							lay.updateViewLayout(guy, g);
						} else {
							if (mr == 1) {
								mr++;
								guy.setBackgroundResource(R.drawable.rightmove2);
								g.leftMargin = (int) (g.leftMargin + staticmovevalue);
								lay.updateViewLayout(guy, g);
							} else {
								if (mr == 2) {
									mr++;
									guy.setBackgroundResource(R.drawable.rightmove3);
									g.leftMargin = (int) (g.leftMargin + staticmovevalue);
									lay.updateViewLayout(guy, g);
								} else {
									if (mr == 3) {
										mr++;
										guy.setBackgroundResource(R.drawable.rightmove4);
										g.leftMargin = (int) (g.leftMargin + staticmovevalue);
										lay.updateViewLayout(guy, g);
									} else {
										if (mr == 4) {
											mr++;
											guy.setBackgroundResource(R.drawable.rightmove5);
											g.leftMargin = (int) (g.leftMargin + staticmovevalue);
											lay.updateViewLayout(guy, g);
										} else {
											if (mr == 5) {
												mr++;
												guy.setBackgroundResource(R.drawable.rightmove6);
												g.leftMargin = (int) (g.leftMargin + staticmovevalue);
												lay.updateViewLayout(guy, g);
											} else {
												if (mr == 6) {
													mr++;
													guy.setBackgroundResource(R.drawable.rightmove7);
													g.leftMargin = (int) (g.leftMargin + staticmovevalue);
													lay.updateViewLayout(guy, g);
												} else {
													if (mr == 7) {
														mr = 0;
														guy.setBackgroundResource(R.drawable.rightmove8);
														g.leftMargin = (int) (g.leftMargin + staticmovevalue);
														lay.updateViewLayout(
																guy, g);
													}

												}
											}
										}
									}
								}
							}
						}
					}

					@Override
					public void onFinish() {
						mr = 0;
						guy.setBackgroundResource(R.drawable.rightstand);

					}
				};
				littlemove.start();

				CountDownTimer cdt = new CountDownTimer(2400, 2400) {

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						play.setVisibility(View.INVISIBLE);
						highscore.setVisibility(View.INVISIBLE);
						rateapp.setVisibility(View.INVISIBLE);
						share.setVisibility(View.INVISIBLE);

						left.setEnabled(true);
						right.setEnabled(true);
						scoretext.setEnabled(true);
						setupHandler();
					}
				};
				cdt.start();

			}
		});

		highscore = new TextView(this);
		highscore.setTypeface(typeFace);
		highscore.setTextColor(Color.parseColor("#fe1414"));
		highscore.setText("HIGH SCORE");
		highscore.setGravity(Gravity.CENTER);
		highscore.setTextSize(50);
		highscore.setShadowLayer((float) 0.01, -2, -2, Color.BLACK);
		hs = new LayoutParams(LayoutParams.WRAP_CONTENT, height * 2 / 15);
		hs.addRule(RelativeLayout.CENTER_HORIZONTAL);
		hs.topMargin = height / 11 + height * 2 / 15;

		rateapp = new TextView(this);
		rateapp.setTypeface(typeFace);
		rateapp.setTextColor(Color.parseColor("#fe1414"));
		rateapp.setText("RATE");
		rateapp.setGravity(Gravity.CENTER);
		rateapp.setShadowLayer((float) 0.01, -2, -2, Color.BLACK);
		rateapp.setTextSize(50);
		rat = new LayoutParams(LayoutParams.WRAP_CONTENT, height * 2 / 15);
		rat.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rat.topMargin = height / 11 + height * 2 / 15 + height * 2 / 15;

		rateapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id="
						+ getApplicationContext().getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

				goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_NEW_DOCUMENT
						| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ getApplicationContext().getPackageName())));
				}
			}
		});

		share = new TextView(this);
		share.setTypeface(typeFace);
		share.setTextColor(Color.parseColor("#fe1414"));
		share.setShadowLayer((float) 0.01, -2, -2, Color.BLACK);
		share.setText("SHARE");
		share.setGravity(Gravity.CENTER);
		share.setTextSize(50);
		s = new LayoutParams(LayoutParams.WRAP_CONTENT, height * 2 / 15);
		s.addRule(RelativeLayout.CENTER_HORIZONTAL);
		s.topMargin = height / 11 + height * 2 / 15 + height * 2 / 15 + height
				* 2 / 15;

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String link = "http://play.google.com/store/apps/details?id="
						+ getApplicationContext().getPackageName();

				Intent share = new Intent(Intent.ACTION_SEND);

				share.setType("text/plain");
				share.putExtra(Intent.EXTRA_TEXT, link);

				startActivityForResult(
						Intent.createChooser(share, "Share this app"), 1);

			}
		});

		panel = new ImageView(this);
		panel.setBackgroundResource(R.drawable.panel);
		pan = new LayoutParams(width, height / 6);
		pan.leftMargin = 0;
		pan.topMargin = height - height / 6 - height / 8;

		left = new Button(this);
		left.setBackgroundResource(R.drawable.leftmovebut);
		l = new LayoutParams(width / 3, height / 6);
		l.leftMargin = 0;
		l.topMargin = height - height / 6 - height / 8;

		right = new Button(this);
		right.setBackgroundResource(R.drawable.rightmovebut);
		r = new LayoutParams(width / 3, height / 6);
		r.leftMargin = width * 2 / 3;
		r.topMargin = height - height / 6 - height / 8;

		guy = new ImageView(this);
		guy.setBackgroundResource(R.drawable.heavy1);
		g = new LayoutParams(width / 4, height / 4);
		g.leftMargin = 0;
		g.topMargin = height - height / 6 - height / 4 - height / 8;

		ads = new TextView(this);
		ads.setText("AD WILL BE DISPLAYED HERE");
		ads.setBackgroundColor(Color.BLACK);
		ads.setGravity(Gravity.CENTER);
		ads.setTextColor(Color.WHITE);
		a = new LayoutParams(width, height / 8);
		a.leftMargin = 0;
		a.topMargin = height - height / 8;

		background = new ImageView(this);
		background.setBackgroundResource(R.drawable.background1);
		b = new LayoutParams(width, height - height / 6 - height / 8);
		b.leftMargin = 0;
		b.topMargin = 0;

		scoretext = new TextView(this);
		scoretext.setTextSize(60);
		scoretext.setTypeface(typeFace);
		scoretext.setGravity(Gravity.CENTER);
		scoretext.setTextColor(Color.RED);

		SpannableString ss1 = new SpannableString("SCORE\n" + score);
		ss1.setSpan(new RelativeSizeSpan(0.5f), 0, 5, 0); // set size

		scoretext.setText(ss1);
		st = new LayoutParams(width / 3, height / 6);
		st.leftMargin = width / 3;
		st.topMargin = height - height / 6 - height / 8;

		lay.addView(background, b);
		lay.addView(panel, pan);
		lay.addView(left, l);
		lay.addView(right, r);
		lay.addView(guy, g);
		lay.addView(ads, a);
		lay.addView(scoretext, st);
		lay.addView(play, p);
		lay.addView(ghostplay, gp);
		lay.addView(highscore, hs);
		lay.addView(share, s);
		lay.addView(rateapp, rat);
		
		left.setVisibility(View.INVISIBLE);
		right.setVisibility(View.INVISIBLE);
		scoretext.setVisibility(View.INVISIBLE);

		guy.setContentDescription("");

		LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View go = inf.inflate(R.layout.gameover, null, false);
		t1 = (TextView) go.findViewById(R.id.textView1);
		t2 = (TextView) go.findViewById(R.id.textView2);
		t3 = (TextView) go.findViewById(R.id.textView3);
		t1.setTypeface(typeFace);
		t2.setTypeface(typeFace);
		t3.setTypeface(typeFace);

		i1 = (ImageView) go.findViewById(R.id.imageView1);
		android.view.ViewGroup.LayoutParams la = i1.getLayoutParams();
		la.height = height / 6;

		Button b1 = (Button) go.findViewById(R.id.button1);
		Button b2 = (Button) go.findViewById(R.id.button2);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// restart button
				score = 0;
				gmover = 0;
				SpannableString ss1 = new SpannableString("SCORE\n" + score);
				ss1.setSpan(new RelativeSizeSpan(0.5f), 0, 5, 0); // set size
				scoretext.setText(ss1);
				br = 0;
				guy.setBackgroundResource(R.drawable.rightstand);
				g.leftMargin = width / 2 - width / 8;
				lay.updateViewLayout(guy, g);
				setupHandler();
				left.setEnabled(true);
				right.setEnabled(true);

				gameover.dismiss();
			}
		});

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// finish button in gameover dialog
				score = 0;
				SpannableString ss1 = new SpannableString("SCORE\n" + score);
				ss1.setSpan(new RelativeSizeSpan(0.5f), 0, 5, 0);
				scoretext.setText(ss1);
				br = 0;
				guy.setBackgroundResource(R.drawable.heavy1);
				g.leftMargin = 0;
				lay.updateViewLayout(guy, g);
				breathing.start();

				RotateAnimation rotate2 = new RotateAnimation(90, 0,
						Animation.RELATIVE_TO_PARENT, 0.5f,
						Animation.RELATIVE_TO_PARENT, 0.5f);
				rotate2.setDuration(2500);
				rotate2.setRepeatCount(0);

				play.setAnimation(rotate2);
				highscore.setAnimation(rotate2);
				rateapp.setAnimation(rotate2);
				share.setAnimation(rotate2);

				play.setVisibility(View.VISIBLE);
				highscore.setVisibility(View.VISIBLE);
				rateapp.setVisibility(View.VISIBLE);
				share.setVisibility(View.VISIBLE);

				RotateAnimation rotate = new RotateAnimation(0, -90,
						Animation.RELATIVE_TO_PARENT, 0.5f,
						Animation.RELATIVE_TO_PARENT, 0.5f);
				rotate.setDuration(2500);
				rotate.setRepeatCount(0);

				left.setAnimation(rotate);
				right.setAnimation(rotate);
				scoretext.setAnimation(rotate);

				left.setEnabled(false);
				right.setEnabled(false);
				scoretext.setEnabled(false);
				play.setEnabled(false);

				CountDownTimer cdt = new CountDownTimer(2400, 2400) {

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						left.setVisibility(View.INVISIBLE);
						right.setVisibility(View.INVISIBLE);
						scoretext.setVisibility(View.INVISIBLE);
						ghostplay.setEnabled(true);
						ghostplay.setVisibility(View.VISIBLE);
						gmover = 0;
						play.setEnabled(true);
						highscore.setEnabled(true);
						rateapp.setEnabled(true);
						share.setEnabled(true);
					}
				};
				cdt.start();

				gameover.dismiss();

			}
		});

		gameover = new AlertDialog.Builder(MainActivity.this).create();
		gameover.setCancelable(false);
		gameover.setView(go, 0, 0, 0, 0);

		View v2 = inf.inflate(R.layout.highscore, null, false);
		hscore = (TextView) v2.findViewById(R.id.textView2);
		TextView hh = (TextView) v2.findViewById(R.id.textView1);

		hscore.setTypeface(typeFace);
		hh.setTypeface(typeFace);

		ImageView thumbs = (ImageView) v2.findViewById(R.id.imageView1);

		android.view.ViewGroup.LayoutParams lej = thumbs.getLayoutParams();
		lej.height = height / 7;
		lej.width = width / 2;

		best = new AlertDialog.Builder(MainActivity.this).create();
		best.setView(v2, 0, 0, 0, 0);

		highscore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pref1 = getApplicationContext().getSharedPreferences(
						"highscore", MODE_PRIVATE);
				int bestscore = pref1.getInt("bestscore", 0);
				hscore.setText("" + bestscore);
				best.show();
				// mediaPlayer.start();
			}
		});

		breathing = new CountDownTimer(999999999, 75) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (br == 0) {
					guy.setBackgroundResource(R.drawable.heavy1);
					br++;
				} else {
					if (br == 1) {
						guy.setBackgroundResource(R.drawable.heavy2);
						br++;
					} else {
						if (br == 2) {
							guy.setBackgroundResource(R.drawable.heavy3);
							br++;
						} else {
							if (br == 3) {
								guy.setBackgroundResource(R.drawable.heavy4);
								br++;
							} else {
								if (br == 4) {
									guy.setBackgroundResource(R.drawable.heavy5);
									br++;
								} else {
									if (br == 5) {
										guy.setBackgroundResource(R.drawable.heavy6);
										br++;
									} else {
										if (br == 6) {
											br++;
										} else {
											if (br == 7) {
												guy.setBackgroundResource(R.drawable.heavy5);
												br++;
											} else {
												if (br == 8) {
													guy.setBackgroundResource(R.drawable.heavy4);
													br++;
												} else {
													if (br == 9) {
														guy.setBackgroundResource(R.drawable.heavy3);
														br++;
													} else {
														if (br == 10) {
															guy.setBackgroundResource(R.drawable.heavy2);
															br++;
														} else {
															if (br == 11) {
																guy.setBackgroundResource(R.drawable.heavy1);
																br = 0;
																;
															}

														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}
		};

		breathing.start();

		moveleft = new CountDownTimer(99999999, 50) {

			@Override
			public void onTick(long millisUntilFinished) {

				if (ml == 0) {

					if (g.leftMargin < -(2 * staticmovevalue)) {
						left.setBackgroundResource(R.drawable.leftmovebut);
						right.setBackgroundResource(R.drawable.rightmovebutgreen);
						leftblock = 0;
						stopleft.start();
						ml = 0;
						moveright.start();
						rightblock++;

					} else {
						guy.setBackgroundResource(R.drawable.leftmove1);
						ml++;
						g.leftMargin = (int) (g.leftMargin - staticmovevalue);
						lay.updateViewLayout(guy, g);

					}

				} else {
					if (ml == 1) {

						if (g.leftMargin < -(2 * staticmovevalue)) {
							left.setBackgroundResource(R.drawable.leftmovebut);
							right.setBackgroundResource(R.drawable.rightmovebutgreen);
							leftblock = 0;
							stopleft.start();
							ml = 0;
							moveright.start();
							rightblock++;

						} else {
							guy.setBackgroundResource(R.drawable.leftmove2);
							ml++;
							g.leftMargin = (int) (g.leftMargin - staticmovevalue);
							lay.updateViewLayout(guy, g);
						}
					} else {
						if (ml == 2) {

							if (g.leftMargin < -(2 * staticmovevalue)) {
								left.setBackgroundResource(R.drawable.leftmovebut);
								right.setBackgroundResource(R.drawable.rightmovebutgreen);
								leftblock = 0;
								stopleft.start();
								ml = 0;
								moveright.start();
								rightblock++;

							} else {
								guy.setBackgroundResource(R.drawable.leftmove3);
								ml++;
								g.leftMargin = (int) (g.leftMargin - staticmovevalue);
								lay.updateViewLayout(guy, g);
							}
						} else {
							if (ml == 3) {

								if (g.leftMargin < -(2 * staticmovevalue)) {
									left.setBackgroundResource(R.drawable.leftmovebut);
									right.setBackgroundResource(R.drawable.rightmovebutgreen);
									leftblock = 0;
									stopleft.start();
									ml = 0;
									moveright.start();
									rightblock++;

								} else {
									guy.setBackgroundResource(R.drawable.leftmove4);
									ml++;
									g.leftMargin = (int) (g.leftMargin - staticmovevalue);
									lay.updateViewLayout(guy, g);
								}
							} else {
								if (ml == 4) {

									if (g.leftMargin < -(2 * staticmovevalue)) {
										left.setBackgroundResource(R.drawable.leftmovebut);
										right.setBackgroundResource(R.drawable.rightmovebutgreen);
										leftblock = 0;
										stopleft.start();
										ml = 0;
										moveright.start();
										rightblock++;

									} else {
										guy.setBackgroundResource(R.drawable.leftmove5);
										ml++;
										g.leftMargin = (int) (g.leftMargin - staticmovevalue);
										lay.updateViewLayout(guy, g);
									}
								} else {
									if (ml == 5) {

										if (g.leftMargin < -(2 * staticmovevalue)) {
											left.setBackgroundResource(R.drawable.leftmovebut);
											right.setBackgroundResource(R.drawable.rightmovebutgreen);
											leftblock = 0;
											stopleft.start();
											ml = 0;
											moveright.start();
											rightblock++;

										} else {
											guy.setBackgroundResource(R.drawable.leftmove6);
											ml++;
											g.leftMargin = (int) (g.leftMargin - staticmovevalue);
											lay.updateViewLayout(guy, g);
										}
									} else {
										if (ml == 6) {

											if (g.leftMargin < -(2 * staticmovevalue)) {
												left.setBackgroundResource(R.drawable.leftmovebut);
												right.setBackgroundResource(R.drawable.rightmovebutgreen);
												leftblock = 0;
												stopleft.start();
												ml = 0;
												moveright.start();
												rightblock++;
											} else {
												guy.setBackgroundResource(R.drawable.leftmove7);
												ml++;
												g.leftMargin = (int) (g.leftMargin - staticmovevalue);
												lay.updateViewLayout(guy, g);
											}
										} else {
											if (ml == 7) {

												if (g.leftMargin < -(2 * staticmovevalue)) {
													left.setBackgroundResource(R.drawable.leftmovebut);
													right.setBackgroundResource(R.drawable.rightmovebutgreen);
													leftblock = 0;
													stopleft.start();
													ml = 0;
													moveright.start();
													rightblock++;

												} else {
													guy.setBackgroundResource(R.drawable.leftmove8);
													ml = 0;
													g.leftMargin = (int) (g.leftMargin - staticmovevalue);
													lay.updateViewLayout(guy, g);
												}
											}

										}
									}
								}
							}
						}
					}
				}

			}

			@Override
			public void onFinish() {
			}
		};

		moveright = new CountDownTimer(99999999, 50) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (mr == 0) {

					if (g.leftMargin >= (width - width / 4) + 2
							* staticmovevalue) {
						right.setBackgroundResource(R.drawable.rightmovebut);
						left.setBackgroundResource(R.drawable.leftmovebutgreen);
						rightblock = 0;
						stopright.start();
						mr = 0;
						moveleft.start();
						leftblock++;

					} else {
						guy.setBackgroundResource(R.drawable.rightmove1);
						mr++;
						g.leftMargin = (int) (g.leftMargin + staticmovevalue);
						lay.updateViewLayout(guy, g);
					}
				} else {
					if (mr == 1) {

						if (g.leftMargin >= (width - width / 4) + 2
								* staticmovevalue) {
							right.setBackgroundResource(R.drawable.rightmovebut);
							left.setBackgroundResource(R.drawable.leftmovebutgreen);
							rightblock = 0;
							stopright.start();
							mr = 0;
							moveleft.start();
							leftblock++;

						} else {
							guy.setBackgroundResource(R.drawable.rightmove2);
							mr++;
							g.leftMargin = (int) (g.leftMargin + staticmovevalue);
							lay.updateViewLayout(guy, g);
						}
					} else {
						if (mr == 2) {

							if (g.leftMargin >= (width - width / 4) + 2
									* staticmovevalue) {
								right.setBackgroundResource(R.drawable.rightmovebut);
								left.setBackgroundResource(R.drawable.leftmovebutgreen);
								rightblock = 0;
								stopright.start();
								mr = 0;
								moveleft.start();
								leftblock++;

							} else {
								guy.setBackgroundResource(R.drawable.rightmove3);
								mr++;
								g.leftMargin = (int) (g.leftMargin + staticmovevalue);
								lay.updateViewLayout(guy, g);
							}
						} else {
							if (mr == 3) {

								if (g.leftMargin >= (width - width / 4) + 2
										* staticmovevalue) {
									right.setBackgroundResource(R.drawable.rightmovebut);
									left.setBackgroundResource(R.drawable.leftmovebutgreen);
									rightblock = 0;
									stopright.start();
									mr = 0;
									moveleft.start();
									leftblock++;

								} else {
									guy.setBackgroundResource(R.drawable.rightmove4);
									mr++;
									g.leftMargin = (int) (g.leftMargin + staticmovevalue);
									lay.updateViewLayout(guy, g);
								}
							} else {
								if (mr == 4) {

									if (g.leftMargin >= (width - width / 4) + 2
											* staticmovevalue) {
										right.setBackgroundResource(R.drawable.rightmovebut);
										left.setBackgroundResource(R.drawable.leftmovebutgreen);
										rightblock = 0;
										stopright.start();
										mr = 0;
										moveleft.start();
										leftblock++;

									} else {
										guy.setBackgroundResource(R.drawable.rightmove5);
										mr++;
										g.leftMargin = (int) (g.leftMargin + staticmovevalue);
										lay.updateViewLayout(guy, g);
									}
								} else {
									if (mr == 5) {

										if (g.leftMargin >= (width - width / 4)
												+ 2 * staticmovevalue) {
											right.setBackgroundResource(R.drawable.rightmovebut);
											left.setBackgroundResource(R.drawable.leftmovebutgreen);
											rightblock = 0;
											stopright.start();
											mr = 0;
											moveleft.start();
											leftblock++;

										} else {
											guy.setBackgroundResource(R.drawable.rightmove6);
											mr++;
											g.leftMargin = (int) (g.leftMargin + staticmovevalue);
											lay.updateViewLayout(guy, g);
										}
									} else {
										if (mr == 6) {

											if (g.leftMargin >= (width - width / 4)
													+ 2 * staticmovevalue) {
												right.setBackgroundResource(R.drawable.rightmovebut);
												left.setBackgroundResource(R.drawable.leftmovebutgreen);
												rightblock = 0;
												stopright.start();
												mr = 0;
												moveleft.start();
												leftblock++;

											} else {
												guy.setBackgroundResource(R.drawable.rightmove7);
												mr++;
												g.leftMargin = (int) (g.leftMargin + staticmovevalue);
												lay.updateViewLayout(guy, g);
											}
										} else {
											if (mr == 7) {

												if (g.leftMargin >= (width - width / 4)
														+ 2 * staticmovevalue) {
													right.setBackgroundResource(R.drawable.rightmovebut);
													left.setBackgroundResource(R.drawable.leftmovebutgreen);
													rightblock = 0;
													stopright.start();
													mr = 0;
													moveleft.start();
													leftblock++;

												} else {
													guy.setBackgroundResource(R.drawable.rightmove8);
													mr = 0;
													g.leftMargin = (int) (g.leftMargin + staticmovevalue);
													lay.updateViewLayout(guy, g);
												}
											}

										}
									}
								}
							}
						}
					}
				}

			}

			@Override
			public void onFinish() {
			}
		};

		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (leftblock == 2) {

				} else {
					if (leftblock == 0) {
						if (rightblock == 1 || rightblock == 2) {
							right.setBackgroundResource(R.drawable.rightmovebut);
						}
						left.setBackgroundResource(R.drawable.leftmovebutgreen);
					}
					if (leftblock == 1) {
						left.setBackgroundResource(R.drawable.leftfastbut);
					}

					if (guy.getContentDescription().equals("lock2")) {
						guy.setContentDescription("");
					}
					rightblock = 0;
					moveright.cancel();
					mr = 0;
					moveleft.start();
					leftblock++;
				}

			}
		});

		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rightblock == 2) {

				} else {
					if (rightblock == 0) {
						if (leftblock == 1 || leftblock == 2) {
							left.setBackgroundResource(R.drawable.leftmovebut);
						}
						right.setBackgroundResource(R.drawable.rightmovebutgreen);
					}
					if (rightblock == 1) {
						right.setBackgroundResource(R.drawable.rightfastbut);
					}
					if (guy.getContentDescription().equals("lock")) {
						guy.setContentDescription("");
					}
					leftblock = 0;
					moveleft.cancel();
					ml = 0;
					moveright.start();
					rightblock++;
				}

			}
		});

		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_CANCELED) {
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetworkInfo = connectivityManager
						.getActiveNetworkInfo();
				try {
					if (activeNetworkInfo.isConnectedOrConnecting() == true) {

					}
				} catch (NullPointerException e1) {
					Toast.makeText(getApplicationContext(),
							"Please check your internet connection",
							Toast.LENGTH_SHORT).show();

				}

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
