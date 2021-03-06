﻿using Android.App;
using Android.Content;
using Android.OS;
using Android.Support.V7.App;
using Android.Views;

namespace ShowcaseAndroid
{
    [Activity(MainLauncher = true)]
    public class MainActivity : AppCompatActivity, View.IOnClickListener
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.MainView);

            FindViewById(Resource.Id.login_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.login_screenlet_demo).SetOnClickListener(this);
            FindViewById(Resource.Id.forgot_password_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.sign_up_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.ddl_form_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.user_portrait_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.web_content_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.asset_list_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.asset_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.image_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.pdf_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.audio_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.video_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.image_gallery_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.comment_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.comment_add_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.comment_list_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.web_content_list_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.rating_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.blogs_display_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.ddl_list_screenlet).SetOnClickListener(this);
            FindViewById(Resource.Id.web_screenlet).SetOnClickListener(this);
        }

        /* IOnClickListener */

        public void OnClick(View v)
        {
            switch (v.Id)
            {
                case Resource.Id.login_screenlet:
                    StartActivity(typeof(LoginActivity));
                    break;
                case Resource.Id.login_screenlet_demo:
                    StartActivity(typeof(LoginDemoActivity));
                    break;
                case Resource.Id.forgot_password_screenlet:
                    StartActivity(typeof(ForgotPasswordActivity));
                    break;
                case Resource.Id.sign_up_screenlet:
                    StartActivity(typeof(SignUpActivity));
        			break;
                case Resource.Id.ddl_form_screenlet:
                    StartActivity(typeof(DDLFormActivity));
        			break;
                case Resource.Id.user_portrait_screenlet:
                    StartActivity(typeof(UserPortraitActivity));
        			break;
                case Resource.Id.web_content_display_screenlet:
                    StartActivity(typeof(WebContentDisplayActivity));
        			break;
                case Resource.Id.asset_list_screenlet:
                    StartActivity(typeof(AssetListActivity));
                    break;
                case Resource.Id.asset_display_screenlet:
                    StartActivity(typeof(AssetDisplayActivity));
                    break;
                case Resource.Id.image_display_screenlet:
                    StartActivity(typeof(ImageDisplayActivity));
                    break;
                case Resource.Id.pdf_display_screenlet:
                    StartActivity(typeof(PdfDisplayActivity));
                    break;
                case Resource.Id.audio_display_screenlet:
                    StartActivity(typeof(AudioDisplayActivity));
                    break;
                case Resource.Id.video_display_screenlet:
                    StartActivity(typeof(VideoDisplayActivity));
                    break;
                case Resource.Id.image_gallery_screenlet:
                    StartActivity(typeof(ImageGalleryActivity));
                    break;
                case Resource.Id.comment_display_screenlet:
                    StartActivity(typeof(CommentDisplayActivity));
                    break;
                case Resource.Id.comment_add_screenlet:
                    StartActivity(typeof(CommentAddActivity));
                    break;
                case Resource.Id.comment_list_screenlet:
                    StartActivity(typeof(CommentListActivity));
                    break;
                case Resource.Id.web_content_list_screenlet:
                    StartActivity(typeof(WebContentListActivity));
                    break;
                case Resource.Id.rating_screenlet:
                    StartActivity(typeof(RatingActivity));
                    break;
                case Resource.Id.blogs_display_screenlet:
                    StartActivity(typeof(BlogsEntryDisplayActivity));
                    break;
                case Resource.Id.ddl_list_screenlet:
                    StartActivity(typeof(DDLListActivity));
                    break;
                case Resource.Id.web_screenlet:
                    StartActivity(typeof(WebScreenletActivity));
                    break;
            }
        }
    }
}