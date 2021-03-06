using System; 
using System.Diagnostics; 
using System.Net; 
using System.Windows.Forms; 
using Microsoft.ApplicationBlocks.ExceptionManagement; 
using NewsComponents; 
using NewsComponents.Net; 
using RssBandit.Resources; 
using RssBandit.WinGui; namespace  RssBandit {
	
    internal partial class  RssBanditApplication {
		
        private  void OnRequestCertificateIssue(object sender, CertificateIssueCancelEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                guiMain.OnRequestCertificateIssue(sender, e);
                            });
        }
 
        private  void OnUpdateFeedsStarted(object sender, NewsHandler.UpdateFeedsEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                if (e.ForcedRefresh)
                                    stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshAllForced);
                                else
                                    stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshAllAuto);
                            });
        }
 
        private  void BeforeDownloadFeedStarted(object sender, NewsHandler.DownloadFeedCancelEventArgs e)
        {
            InvokeOnGui(
                delegate
                    {
                        bool cancel = e.Cancel;
                        guiMain.OnFeedUpdateStart(e.FeedUri, ref cancel);
                        e.Cancel = cancel;
                    });
        }
 
        internal  void OnUpdateFeedStarted(object sender, NewsHandler.UpdateFeedEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshOne);
                            });
        }
 
        internal  void OnUpdatedFeed(object sender, NewsHandler.UpdatedFeedEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                guiMain.UpdateFeed(e.UpdatedFeedUri, e.NewFeedUri, e.UpdateState == RequestResult.OK);
                                if (e.FirstSuccessfulDownload)
                                {
                                    SubscriptionModified(NewsFeedProperty.FeedCacheUrl);
                                }
                                stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshOneDone);
                            });
        }
 
        internal  void OnUpdatedCommentFeed(object sender, NewsHandler.UpdatedFeedEventArgs e)
        {
            if (e.UpdateState == RequestResult.OK)
            {
                InvokeOnGui(delegate
                                {
                                    guiMain.UpdateCommentFeed(e.UpdatedFeedUri, e.NewFeedUri);
                                });
            }
        }
 
        internal  void OnUpdatedFavicon(object sender, NewsHandler.UpdatedFaviconEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                guiMain.UpdateFavicon(e.Favicon, e.FeedUrls);
                            });
        }
 
        internal  void OnDownloadedEnclosure(object sender, DownloadItemEventArgs e)
        {
            if (Preferences.AddPodcasts2WMP)
            {
                AddPodcastToWMP(e.DownloadItem);
            }
            if (Preferences.AddPodcasts2ITunes)
            {
                AddPodcastToITunes(e.DownloadItem);
            }
            InvokeOnGui(delegate
                            {
                                guiMain.OnEnclosureReceived(sender, e);
                            });
        }
 
        internal  void OnUpdateFeedException(object sender, NewsHandler.UpdateFeedExceptionEventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                WebException webex = e.ExceptionThrown as WebException;
                                if (webex != null)
                                {
                                    if (webex.Status == WebExceptionStatus.NameResolutionFailure ||
                                        webex.Status == WebExceptionStatus.ProxyNameResolutionFailure)
                                    {
                                        UpdateInternetConnectionState(true);
                                        if (!InternetAccessAllowed)
                                        {
                                            guiMain.UpdateFeed(e.FeedUri, null, false);
                                            stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshOneDone);
                                            return;
                                        }
                                    }
                                }
                                Trace.WriteLine(e.ExceptionThrown.StackTrace);
                                UpdateXmlFeedErrorFeed(e.ExceptionThrown, e.FeedUri, true);
                                stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshOneDone);
                            });
        }
 
        private  void OnAllCommentFeedRequestsCompleted(object sender, EventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                guiMain.OnAllAsyncUpdateCommentFeedsFinished();
                            });
        }
 
        private  void OnAllRequestsCompleted(object sender, EventArgs e)
        {
            InvokeOnGui(delegate
                            {
                                stateManager.MoveNewsHandlerStateTo(NewsHandlerState.RefreshAllDone);
                                guiMain.TriggerGUIStateOnNewFeeds(true);
                                guiMain.OnAllAsyncUpdateFeedsFinished();
                            });
        }
 
        private  void OnLoadingFeedlistProgress(object sender, ThreadWorkerProgressArgs args)
        {
            if (args.Exception != null)
            {
                args.Cancel = true;
                BanditApplicationException ex = args.Exception as BanditApplicationException;
                if (ex != null)
                {
                    if (ex.Number == ApplicationExceptions.FeedlistOldFormat)
                    {
                        Application.Exit();
                    }
                    else if (ex.Number == ApplicationExceptions.FeedlistOnRead)
                    {
                        ExceptionManager.Publish(ex.InnerException);
                        this.MessageError(SR.ExceptionReadingFeedlistFile(ex.InnerException.Message, GetLogFileName()));
                        this.SetGuiStateFeedbackText(SR.GUIStatusErrorReadingFeedlistFile);
                    }
                    else if (ex.Number == ApplicationExceptions.FeedlistOnProcessContent)
                    {
                        this.MessageError(SR.InvalidFeedlistFileMessage(GetLogFileName()));
                        this.SetGuiStateFeedbackText(SR.GUIStatusValidationErrorReadingFeedlistFile);
                    }
                    else if (ex.Number == ApplicationExceptions.FeedlistNA)
                    {
                        this.refreshRate = feedHandler.RefreshRate;
                        this.SetGuiStateFeedbackText(SR.GUIStatusNoFeedlistFile);
                    }
                    else
                    {
                        PublishException(args.Exception);
                        this.SetGuiStateFeedbackText(SR.GUIStatusErrorReadingFeedlistFile);
                    }
                }
                else
                {
                    PublishException(args.Exception);
                    this.SetGuiStateFeedbackText(SR.GUIStatusErrorReadingFeedlistFile);
                }
            }
            else if (!args.Done)
            {
                if (!IsFormAvailable(guiMain))
                {
                    args.Cancel = true;
                    return;
                }
                this.SetGuiStateFeedbackText(SR.GUIStatusLoadingFeedlist);
            }
            else if (args.Done)
            {
                this.refreshRate = feedHandler.RefreshRate;
                this.CheckAndMigrateSettingsAndPreferences();
                this.CheckAndMigrateListViewLayouts();
                this.feedHandler.ResumePendingDownloads();
                if (!IsFormAvailable(guiMain))
                {
                    args.Cancel = true;
                    return;
                }
                try
                {
<<<<<<< /work/joliebig/semistructured_merge/fse2011_artifact/binary/fstmerge_tmp1307442437616/fstmerge_var1_5624129645652932292
                    this.guiMain.PopulateFeedSubscriptions(feedHandler.GetCategories().Values, feedHandler.FeedsTable,
=======
                    this.guiMain.PopulateFeedSubscriptions(feedHandler.Categories, feedHandler.FeedsTable,
>>>>>>> /work/joliebig/semistructured_merge/fse2011_artifact/binary/fstmerge_tmp1307442437616/fstmerge_var2_1795896785886842973
                                                           DefaultCategory);
                }
                catch (Exception ex)
                {
                    PublishException(ex);
                }
                if (FeedlistLoaded != null)
                    FeedlistLoaded(this, EventArgs.Empty);
                this.SetGuiStateFeedbackText(SR.GUIStatusDone);
                foreach (string newFeedUrl in this.commandLineOptions.SubscribeTo)
                {
                    if (IsFormAvailable(guiMain))
                        this.guiMain.AddFeedUrlSynchronized(newFeedUrl);
                }
                guiMain.UpdateAllFeeds(this.Preferences.FeedRefreshOnStartup);
            }
        }

	}

}
