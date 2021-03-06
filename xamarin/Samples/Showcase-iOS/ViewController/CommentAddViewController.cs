﻿using Foundation;
using LiferayScreens;
using System;
using UIKit;

namespace ShowcaseiOS.ViewController
{
    public partial class CommentAddViewController : UIViewController, ICommentAddScreenletDelegate
    {
        public CommentAddViewController(IntPtr handle) : base(handle) { }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            this.commentAddScreenlet.ClassPK = LiferayServerContext.LongPropertyForKey("commentClassPK");
            this.commentAddScreenlet.ClassName = LiferayServerContext.StringPropertyForKey("fileClassName");

            this.commentAddScreenlet.Delegate = this;
        }

        /* ICommentAddScreenletDelegate */


        [Export("screenlet:onAddCommentError:")]
        public virtual void OnAddCommentError(CommentAddScreenlet screenlet, NSError error)
        {
            Console.WriteLine($"Comment add failed: {error.DebugDescription}");
        }

        [Export("screenlet:onCommentAdded:")]
        public virtual void OnCommentAdded(CommentAddScreenlet screenlet, Comment comment)
        {
            Console.WriteLine($"Comment add success: {comment.CommentId}");
        }

        [Export("screenlet:onCommentUpdated:")]
        public virtual void OnCommentUpdated(CommentAddScreenlet screenlet, Comment comment)
        {
            Console.WriteLine($"Comment update success: {comment.CommentId}");
        }

        [Export("screenlet:onUpdateCommentError:")]
        public virtual void OnUpdateCommentError(CommentAddScreenlet screenlet, NSError error)
        {
            Console.WriteLine($"Comment update failed: {error.DebugDescription}");
        }
    }
}

