﻿using Foundation;
using LiferayScreens;
using System;
using UIKit;

namespace ShowcaseiOS.ViewController
{
    public partial class ImageDisplayViewController : UIViewController, IFileDisplayScreenletDelegate
    {
        public ImageDisplayViewController(IntPtr handle) : base(handle) { }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            this.imageDisplayScreenlet.AutoLoad = false;
            this.imageDisplayScreenlet.AssetEntryId = LiferayServerContext.LongPropertyForKey("imageEntryId");

            this.imageDisplayScreenlet.Delegate = this;
            this.imageDisplayScreenlet.Load();
        }

        /* IFileDisplayScreenletDelegate */

        [Export("screenlet:onFileAssetError:")]
        public virtual void OnFileAssetError(FileDisplayScreenlet screenlet, NSError error)
        {
            Console.WriteLine($"Image display failed: {error.DebugDescription}");
        }

        [Export("screenlet:onFileAssetResponse:")]
        public virtual void OnFileAssetResponse(FileDisplayScreenlet screenlet, NSUrl url)
        {
            Console.WriteLine($"Image display success: {url.DebugDescription}");
        }
    }
}

