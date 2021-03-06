using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Web;
using System.Windows.Forms;
using NewsComponents;
using NewsComponents.Feed;
using NewsComponents.Utils;
using RssBandit.Resources;
using RssBandit.WinGui;
using RssBandit.WinGui.Controls;
using RssBandit.WinGui.Dialogs;
using RssBandit.WinGui.Forms;
using RssBandit.WinGui.Interfaces;
using RssBandit.WinGui.Menus;
using RssBandit.WinGui.Utility;
using RssBandit.Common;
namespace RssBandit
{
    internal partial class RssBanditApplication
    {
        public void CmdCatchUpCurrentSelectedNode(ICommand sender)
        {
            guiMain.MarkSelectedNodeRead(guiMain.CurrentSelectedFeedsNode);
            if (guiMain.CurrentSelectedFeedsNode != null)
                FeedWasModified(guiMain.CurrentSelectedFeedsNode.DataKey, NewsFeedProperty.FeedItemReadState);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdDeleteAllFeedItems(ICommand sender)
        {
            TreeFeedsNodeBase tn = guiMain.CurrentSelectedFeedsNode;
            if (tn != null)
            {
                ISmartFolder isFolder = tn as ISmartFolder;
                if (isFolder != null)
                {
                    if (isFolder is FlaggedItemsNode || isFolder is WatchedItemsNode)
                    {
                        for (int i = 0, j = isFolder.Items.Count; i < j; i++)
                        {
                            INewsItem item = isFolder.Items[0];
                            RemoveItemFromSmartFolder(isFolder, item);
                        }
                    }
                    else
                    {
                        isFolder.Items.Clear();
                    }
                    isFolder.Modified = true;
                    guiMain.PopulateSmartFolder(tn, true);
                    guiMain.UpdateTreeNodeUnreadStatus(tn, 0);
                    return;
                }
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdDeleteSelectedFeedItems(ICommand sender)
        {
            if (guiMain.CurrentSelectedFeedsNode != null)
            {
                guiMain.RemoveSelectedFeedItems();
            }
        }
        public void CmdRestoreSelectedFeedItems(ICommand sender)
        {
            if (guiMain.CurrentSelectedFeedsNode != null)
            {
                guiMain.RestoreSelectedFeedItems();
            }
        }
        public bool CmdNewFeed(string category, string feedLink, string feedTitle)
        {
            return SubscribeToFeed(feedLink, category, feedTitle);
        }
        public void CmdExitApp(ICommand sender)
        {
            if (guiMain != null)
            {
                guiMain.Close(true);
            }
            Application.Exit();
        }
        public void CmdShowAlertWindowNone(ICommand sender)
        {
            Mediator.SetChecked(
                "+cmdShowAlertWindowNone",
                "-cmdShowAlertWindowConfiguredFeeds",
                "-cmdShowAlertWindowAll");
            Preferences.ShowAlertWindow = DisplayFeedAlertWindow.None;
            SavePreferences();
        }
        public void CmdShowAlertWindowConfigPerFeed(ICommand sender)
        {
            Mediator.SetChecked(
                "-cmdShowAlertWindowNone",
                "+cmdShowAlertWindowConfiguredFeeds",
                "-cmdShowAlertWindowAll");
            Preferences.ShowAlertWindow = DisplayFeedAlertWindow.AsConfiguredPerFeed;
            SavePreferences();
        }
        public void CmdShowAlertWindowAll(ICommand sender)
        {
            Mediator.SetChecked(
                "-cmdShowAlertWindowNone",
                "-cmdShowAlertWindowConfiguredFeeds",
                "+cmdShowAlertWindowAll");
            Preferences.ShowAlertWindow = DisplayFeedAlertWindow.All;
            SavePreferences();
        }
        public void CmdToggleShowNewItemsReceivedBalloon(ICommand sender)
        {
            bool currentChecked;
            if (sender != null)
                currentChecked = ((ICommandComponent) sender).Checked;
            else
                currentChecked = Mediator.IsChecked("cmdShowNewItemsReceivedBalloon");
            Preferences.ShowNewItemsReceivedBalloon = !currentChecked;
            Mediator.SetChecked(!currentChecked, "cmdShowNewItemsReceivedBalloon");
            SavePreferences();
        }
        public void CmdToggleInternetConnectionMode(ICommand sender)
        {
            bool currentChecked;
            if (sender != null)
                currentChecked = ((ICommandComponent) sender).Checked;
            else
                currentChecked = Mediator.IsChecked("cmdToggleOfflineMode");
            Utils.SetIEOffline(currentChecked);
            UpdateInternetConnectionState(true);
        }
        public void CmdAboutApp(ICommand sender)
        {
            MessageBox.Show(Caption + " written by\n\n" +
                            "  * Dare Obasanjo (DareObasanjo, www.25hoursaday.com/weblog/)\n" +
                            "  * Torsten Rendelmann (TorstenR, www.rendelmann.info/blog/)\n" +
                            "  * Phil Haack (haacked.com)\n" +
                            "  * and all the active members of RSS Bandit community.\n" +
                            "\nCredits:\n\n" +
                            "  * Mike Krueger (#ZipLib)\n" +
                            "  * Jack Palevich (NntpClient)\n" +
                            "  * NetAdvantage for Windows Forms (c) 2006 by Infragistics, http://www.infragistics.com\n" +
                            "  * SandBar, SandDock (c) 2005 by Divelements Limited, http://www.divil.co.uk/net/\n" +
                            "  * Portions Copyright Â©2002-2004 The Genghis Group (www.genghisgroup.com)\n" +
                            "  * sourceforge.net team (Project hosting)", SR.WindowAboutCaption(CaptionOnly),
                            MessageBoxButtons.OK, MessageBoxIcon.Asterisk);
        }
        public void CmdWebHelp(ICommand sender)
        {
            NavigateToUrlInExternalBrowser(webHelpUrl);
        }
        public void CmdReportAppBug(ICommand sender)
        {
            NavigateToUrlAsUserPreferred(bugReportUrl, CaptionOnly + ": Bug Tracker", true, true);
        }
        public void CmdWorkspaceNews(ICommand sender)
        {
            NavigateToUrlAsUserPreferred(workspaceNewsUrl, CaptionOnly + ": Project News", true, true);
        }
        public void CmdWikiNews(ICommand sender)
        {
            NavigateToUrlAsUserPreferred(wikiNewsUrl, CaptionOnly + ": Wiki", true, true);
        }
        public void CmdVisitForum(ICommand sender)
        {
            NavigateToUrlAsUserPreferred(forumUrl, CaptionOnly + ": Forum", true, true);
        }
        public void CmdDonateToProject(ICommand sender)
        {
            NavigateToUrlAsUserPreferred(projectDonationUrl, CaptionOnly + ": Donate", true, true);
        }
        public void CmdSendLogsByMail(ICommand sender)
        {
            List<string> files = new List<string>();
            try
            {
                string logFilesFolder =
                    Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), Name);
                if (Directory.Exists(logFilesFolder))
                {
                    string[] matches = Directory.GetFiles(logFilesFolder, "trace.log*");
                    if (matches.Length > 0)
                    {
                        files.AddRange(matches);
                        if (Preferences.UseProxy)
                        {
                            files.Add(GetPreferencesFileName());
                        }
                        string zipDest = Path.Combine(
                            Environment.GetFolderPath(Environment.SpecialFolder.Personal),
                            "RssBandit.logs." + VersionLong + ".zip");
                        if (File.Exists(zipDest))
                            FileHelper.Delete(zipDest);
                        FileHelper.ZipFiles(files.ToArray(), zipDest);
                        Process.Start(CreateMailUrlMessage(
                                          "contact@rssbandit.org",
                                          "Log files -- RSS Bandit v" + VersionLong,
                                          "Please attach this file from your My Documents folder:\r\n\n" + zipDest +
                                          "\r\n\r\n" +
                                          "and replace this text with some more useful informations about: \r\n" +
                                          "\t* Your system environment and OS version\r\n" +
                                          "\t* Description of the issue to report\r\n" +
                                          "\t* Any hints/links that may help,\r\nplease!"));
                    }
                    else
                    {
                        MessageInfo("No log files at " + logFilesFolder);
                    }
                }
                else
                {
                    MessageInfo("No log files at " + logFilesFolder);
                }
            }
            catch (Exception ex)
            {
                _log.Error("Failed to send log files", ex);
                MessageError("Failed to send log files: \r\n" + ex.Message);
            }
        }
        private static string CreateMailUrlMessage(string to, string subject, string text)
        {
            subject = HtmlHelper.UrlEncode(subject);
            string body = HtmlHelper.UrlEncode(text);
            if (body.Length + subject.Length > 900)
            {
                if (subject.Length > 400)
                {
                    subject = subject.Substring(0, 400) + "...";
                }
                body = body.Substring(0, 897 - subject.Length) + "...";
            }
            return "mailto:" + to + "?subject=" + subject + "&body=" + body;
        }
        public void CmdCheckForUpdates(ICommand sender)
        {
            CmdCheckForUpdates(AutoUpdateMode.Manually);
        }
        public void CmdCheckForUpdates(AutoUpdateMode mode)
        {
            if (mode == AutoUpdateMode.Manually)
                CheckForUpdates(mode);
            else
            {
                if (!InternetAccessAllowed)
                    return;
                if (Preferences.AutoUpdateFrequency == AutoUpdateMode.Manually)
                    return;
                if (Preferences.AutoUpdateFrequency == AutoUpdateMode.OnApplicationStart &&
                    mode == AutoUpdateMode.OnApplicationStart)
                {
                    CheckForUpdates(mode);
                    return;
                }
                DateTime t = LastAutoUpdateCheck;
                if (Preferences.AutoUpdateFrequency == AutoUpdateMode.OnceIn14Days)
                    t = t.AddDays(14);
                else
                    t = t.AddMonths(1);
                if (DateTime.Compare(t, DateTime.Now) < 0)
                    CheckForUpdates(mode);
            }
        }
        private void OnApplicationUpdateAvailable(object sender, UpdateAvailableEventArgs e)
        {
            AutoUpdateMode mode = (AutoUpdateMode) RssBanditUpdateManager.Tag;
            bool hasUpdates = e.NewVersionAvailable;
            if (hasUpdates)
            {
                if (DialogResult.No == MessageQuestion(SR.DialogQuestionNewAppVersionAvailable))
                {
                    LastAutoUpdateCheck = DateTime.Now;
                }
                else
                {
                    NavigateToUrlAsUserPreferred(projectDownloadUrl, CaptionOnly + ": Download", true, true);
                    LastAutoUpdateCheck = DateTime.Now;
                }
            }
            else
            {
                LastAutoUpdateCheck = DateTime.Now;
                if (mode == AutoUpdateMode.Manually)
                    MessageInfo(SR.DialogMessageNoNewAppVersionAvailable);
            }
        }
        private void CheckForUpdates(AutoUpdateMode mode)
        {
            try
            {
                RssBanditUpdateManager.Tag = mode;
                if (mode == AutoUpdateMode.Manually)
                    RssBanditUpdateManager.BeginCheckForUpdates(guiMain, Proxy);
                else
                    RssBanditUpdateManager.BeginCheckForUpdates(null, Proxy);
            }
            catch (Exception ex)
            {
                _log.Error("RssBanditUpdateManager.BeginCheckForUpdates() failed", ex);
            }
        }
        public void CmdShowMainGui(ICommand sender)
        {
            InvokeOnGui(delegate
                            {
                                guiMain.DoShow();
                            });
        }
        public void CmdRefreshFeeds(ICommand sender)
        {
            guiMain.UpdateAllFeeds(true);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNewFeed(ICommand sender)
        {
            string category = guiMain.CategoryOfSelectedNode();
            if (category == null)
            {
                category = DefaultCategory;
            }
            SubscribeToFeed(null, category.Trim(), null, null, WizardMode.SubscribeURLDirect);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNewSubscription(ICommand sender)
        {
            string category = guiMain.CategoryOfSelectedNode();
            if (category == null)
            {
                category = DefaultCategory;
            }
            SubscribeToFeed(null, category.Trim(), null, null, WizardMode.Default);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNewNntpFeed(ICommand sender)
        {
            string category = guiMain.CategoryOfSelectedNode();
            if (category == null)
            {
                category = DefaultCategory;
            }
            SubscribeToFeed(null, category.Trim(), null, null, WizardMode.SubscribeNNTPDirect);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNextUnreadFeedItem(ICommand sender)
        {
            guiMain.MoveToNextUnreadItem();
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdOpenManageAddInsDialog(ICommand sender)
        {
            ManageAddInDialog dialog = new ManageAddInDialog(this);
            dialog.ShowDialog(MainForm);
            dialog.Dispose();
        }
        public void CmdTopStories(ICommand sender)
        {
            string memeFile = GetTopStoriesFileName();
            TopStoriesThreadHandler th = new TopStoriesThreadHandler(this, memeFile);
            DialogResult result = th.Start(guiMain, SR.ProcessTopStoriesEntertainmentWaitMessage, true);
            if (result != DialogResult.OK)
                return;
            if (!th.OperationSucceeds)
            {
                MessageError("The following error occured while determining Top Stories" +
                             th.OperationException.Message);
                return;
            }
            NavigateToUrl(memeFile, null, true, true);
        }
        public void CmdAutoDiscoverFeed(ICommand sender)
        {
            if (SearchForFeeds(null))
            {
            }
            return;
        }
        public void CmdNewCategory(ICommand sender)
        {
            guiMain.NewCategory();
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdDeleteAll(ICommand sender)
        {
            if (MessageQuestion(SR.MessageBoxDeleteAllFeedsQuestion) == DialogResult.Yes)
            {
                feedHandler.DeleteAllFeedsAndCategories();
                FeedSource.SearchHandler.IndexRemoveAll();
                SubscriptionModified(NewsFeedProperty.General);
                guiMain.InitiatePopulateTreeFeeds();
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdUpdateCategory(ICommand sender)
        {
            guiMain.UpdateCategory(true);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdRenameCategory(ICommand sender)
        {
            guiMain.InitiateRenameFeedOrCategory();
        }
        public void CmdDeleteCategory(ICommand sender)
        {
            if (guiMain.NodeEditingActive)
                return;
            TreeFeedsNodeBase tn = guiMain.CurrentSelectedFeedsNode;
            if (tn == null) return;
            if (tn.Type != FeedNodeType.Category) return;
            if (DialogResult.Yes == MessageQuestion(
                                        SR.MessageBoxDeleteAllFeedsInCategoryQuestion,
                                        String.Format(" - {0} ({1})", SR.MenuDeleteCategoryCaption,
                                                      guiMain.CurrentSelectedFeedsNode.Text)))
            {
                guiMain.DeleteCategory(tn);
                SubscriptionModified(NewsFeedProperty.FeedCategoryRemoved);
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdUpdateFeed(ICommand sender)
        {
            string feedUrl = guiMain.CurrentSelectedFeedsNode.DataKey;
            if (!string.IsNullOrEmpty(feedUrl))
            {
                feedHandler.AsyncGetItemsForFeed(feedUrl, true, true);
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdRenameFeed(ICommand sender)
        {
            guiMain.InitiateRenameFeedOrCategory();
        }
        public void CmdViewSourceOfFeed(ICommand sender)
        {
            if (guiMain.CurrentSelectedFeedsNode != null && guiMain.CurrentSelectedFeedsNode.DataKey != null)
            {
                string feedUrl = guiMain.CurrentSelectedFeedsNode.DataKey;
                string title = SR.TabFeedSourceCaption(guiMain.CurrentSelectedFeedsNode.Text);
                using (
                    FeedSourceDialog dialog =
                        new FeedSourceDialog(Proxy, feedHandler.GetFeedCredentials(feedUrl), feedUrl, title))
                {
                    dialog.ShowDialog(guiMain);
                }
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdValidateFeed(ICommand sender)
        {
            CmdValidateFeed(guiMain.CurrentSelectedFeedsNode.DataKey);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdValidateFeed(string feedLink)
        {
            if (!string.IsNullOrEmpty(feedLink))
            {
                NavigateToUrlAsUserPreferred(validationUrlBase + HttpUtility.UrlEncode(feedLink),
                                             SR.TabValidationResultCaption, true, true);
            }
        }
        public void CmdNavigateFeedHome(ICommand sender)
        {
            CmdNavigateFeedHome(guiMain.CurrentSelectedFeedsNode.DataKey);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNavigateFeedHome(string feedLink)
        {
            if (!string.IsNullOrEmpty(feedLink))
            {
                IFeedDetails feedInfo = feedHandler.GetFeedDetails(feedLink);
                if (feedInfo != null)
                {
                    NavigateToUrlAsUserPreferred(feedInfo.Link, SR.TabFeedHomeCaption(feedInfo.Title), true, true);
                }
            }
        }
        public void CmdNavigateFeedLinkCosmos(ICommand sender)
        {
            CmdNavigateFeedLinkCosmos(guiMain.CurrentSelectedFeedsNode.DataKey);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdNavigateFeedLinkCosmos(string feedLink)
        {
            if (!string.IsNullOrEmpty(feedLink))
            {
                IFeedDetails feedInfo = feedHandler.GetFeedDetails(feedLink);
                if (feedInfo != null)
                {
                    NavigateToUrlAsUserPreferred(linkCosmosUrlBase + HttpUtility.UrlEncode(feedInfo.Link),
                                                 SR.TabLinkCosmosCaption(feedInfo.Title), true, true);
                }
            }
        }
        public void CmdExportFeeds(ICommand sender)
        {
            ExportFeedsDialog dialog =
                new ExportFeedsDialog(guiMain.GetRoot(RootFolderType.MyFeeds), Preferences.NormalFont,
                                      guiMain.TreeImageList);
            if (DialogResult.OK == dialog.ShowDialog(guiMain))
            {
                Stream myStream;
                SaveFileDialog sfd = new SaveFileDialog();
                ArrayList selections = dialog.GetSelectedFeedUrls();
                IDictionary<string, INewsFeed> fc = new SortedDictionary<string, INewsFeed>();
                foreach (string url in selections)
                {
                    if (feedHandler.IsSubscribed(url))
                        fc.Add(url, feedHandler.GetFeeds()[url]);
                }
                if (fc.Count == 0)
                    fc = feedHandler.GetFeeds();
                bool includeEmptyCategories = false;
                FeedListFormat format = FeedListFormat.OPML;
                String.Format("{0} (*.*)|*.*", SR.FileDialogFilterAllFiles);
                if (dialog.radioFormatOPML.Checked)
                {
                    format = FeedListFormat.OPML;
                    sfd.Filter =
                        String.Format("{0} (*.opml)|*.opml|{1} (*.*)|*.*", SR.FileDialogFilterOPMLFiles,
                                      SR.FileDialogFilterAllFiles);
                    includeEmptyCategories = dialog.checkFormatOPMLIncludeCats.Checked;
                }
                else if (dialog.radioFormatNative.Checked)
                {
                    format = FeedListFormat.NewsHandler;
                    sfd.Filter =
                        String.Format("{0} (*.xml)|*.xml|{1} (*.*)|*.*", SR.FileDialogFilterXMLFiles,
                                      SR.FileDialogFilterAllFiles);
                    if (!dialog.checkFormatNativeFull.Checked)
                        format = FeedListFormat.NewsHandlerLite;
                }
                sfd.FilterIndex = 1;
                sfd.RestoreDirectory = true;
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    try
                    {
                        if ((myStream = sfd.OpenFile()) != null)
                        {
                            feedHandler.SaveFeedList(myStream, format, fc, includeEmptyCategories);
                            myStream.Close();
                        }
                    }
                    catch (Exception ex)
                    {
                        MessageError(SR.ExceptionSaveFileMessage(sfd.FileName, ex.Message));
                    }
                }
            }
            dialog.Dispose();
        }
        public void CmdImportFeeds(ICommand sender)
        {
            string category = String.Empty;
            TreeFeedsNodeBase n = guiMain.CurrentSelectedFeedsNode;
            if (n != null)
            {
                if (n.Type == FeedNodeType.Category || n.Type == FeedNodeType.Feed)
                    category = n.CategoryStoreName;
            }
            ImportFeeds(String.Empty, category);
        }
        public void CmdUploadFeeds(ICommand sender)
        {
            if (!Preferences.UseRemoteStorage)
            {
                MessageInfo(SR.RemoteStorageFeature_Info);
                return;
            }
            if (MessageQuestion(SR.RemoteStorageUpload_Question) == DialogResult.No)
            {
                return;
            }
            RemoteFeedlistThreadHandler rh = new RemoteFeedlistThreadHandler(
                RemoteFeedlistThreadHandler.Operation.Upload, this,
                Preferences.RemoteStorageProtocol, Preferences.RemoteStorageLocation,
                Preferences.RemoteStorageUserName, Preferences.RemoteStoragePassword, GuiSettings);
            DialogResult result =
                rh.Start(guiMain, SR.GUIStatusWaitMessageUpLoadingFeedlist(Preferences.RemoteStorageProtocol.ToString()),
                         false);
            if (result != DialogResult.OK)
                return;
            if (!rh.OperationSucceeds)
            {
                MessageError(SR.GUIFeedlistUploadExceptionMessage(rh.OperationException.Message));
            }
        }
        public void CmdDownloadFeeds(ICommand sender)
        {
            if (!Preferences.UseRemoteStorage)
            {
                MessageInfo(SR.Keys.RemoteStorageFeature_Info);
                return;
            }
            if (MessageQuestion(SR.RemoteStorageDownload_Question) == DialogResult.No)
            {
                return;
            }
            RemoteFeedlistThreadHandler rh = new RemoteFeedlistThreadHandler(
                RemoteFeedlistThreadHandler.Operation.Download, this,
                Preferences.RemoteStorageProtocol, Preferences.RemoteStorageLocation,
                Preferences.RemoteStorageUserName, Preferences.RemoteStoragePassword, GuiSettings);
            DialogResult result =
                rh.Start(guiMain,
                         SR.GUIStatusWaitMessageDownLoadingFeedlist(Preferences.RemoteStorageProtocol.ToString()), false);
            if (result != DialogResult.OK)
                return;
            if (rh.OperationSucceeds)
            {
                guiMain.SaveSubscriptionTreeState();
                guiMain.SyncFinderNodes();
                guiMain.InitiatePopulateTreeFeeds();
                guiMain.LoadAndRestoreSubscriptionTreeState();
            }
            else
            {
                MessageError(SR.GUIFeedlistDownloadExceptionMessage(rh.OperationException.Message));
            }
        }
        public void CmdShowOptions(ICommand sender)
        {
            ShowOptions(OptionDialogSection.Default, guiMain, null);
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdShowFeedProperties(ICommand sender)
        {
            if (guiMain.CurrentSelectedFeedsNode != null && guiMain.CurrentSelectedFeedsNode.DataKey != null)
            {
                TreeFeedsNodeBase tn = guiMain.CurrentSelectedFeedsNode;
                INewsFeed f;
    int refreshrate = Preferences.RefreshRate;
                TimeSpan feedMaxItemAge = TimeSpan.Zero;
                bool feedDisabled = false;
                bool feedMarkItemsReadOnExit = false;
                try
                {
                    f = feedHandler.GetFeeds()[tn.DataKey];
                    try
                    {
                        refreshrate = feedHandler.GetRefreshRate(f.link);
                        feedMaxItemAge = feedHandler.GetMaxItemAge(f.link);
                        feedMarkItemsReadOnExit = feedHandler.GetMarkItemsReadOnExit(f.link);
                    }
                    catch
                    {
                    }
                }
                catch (Exception e)
                {
                    MessageError(SR.GUIStatusErrorGeneralFeedMessage(tn.DataKey, e.Message));
                    return;
                }
                FeedProperties propertiesDialog =
                    new FeedProperties(f.title, f.link, refreshrate/MilliSecsMultiplier, feedMaxItemAge,
                                       (f.category ?? defaultCategory), defaultCategory,
                                       feedHandler.GetCategories().Keys, feedHandler.GetStyleSheet(f.link));
                propertiesDialog.comboMaxItemAge.Enabled = !feedMaxItemAge.Equals(TimeSpan.Zero);
                propertiesDialog.checkEnableAlerts.Checked = f.alertEnabled;
                propertiesDialog.checkMarkItemsReadOnExit.Checked = feedMarkItemsReadOnExit;
                propertiesDialog.checkDownloadEnclosures.Checked = feedHandler.GetDownloadEnclosures(f.link);
                propertiesDialog.checkEnableEnclosureAlerts.Checked = feedHandler.GetEnclosureAlert(f.link);
                if (f.authUser != null)
                {
                    string u = null, p = null;
                    FeedSource.GetFeedCredentials(f, ref u, ref p);
                    propertiesDialog.textUser.Text = u;
                    propertiesDialog.textPwd.Text = p;
                }
                propertiesDialog.ShowDialog(guiMain);
                if (propertiesDialog.DialogResult == DialogResult.OK)
                {
                    NewsFeedProperty changes = NewsFeedProperty.None;
                    bool refreshThisFeed = false;
                    if ((propertiesDialog.textBox1.Text == null) ||
                        (propertiesDialog.textBox2.Text == null) ||
                        propertiesDialog.textBox1.Text.Trim().Equals(String.Empty) ||
                        propertiesDialog.textBox2.Text.Trim().Equals(String.Empty))
                    {
                        MessageError(SR.GUIFieldLinkTitleInvalid);
                    }
                    else
                    {
                        if (!f.link.Equals(propertiesDialog.textBox2.Text.Trim()))
                        {
                            feedHandler.GetFeeds().Remove(f.link);
                            changes |= NewsFeedProperty.FeedLink;
                            string newLink = propertiesDialog.textBox2.Text.Trim();
                            try
                            {
                                Uri reqUri = new Uri(newLink);
                                newLink = reqUri.CanonicalizedUri();
                            }
                            catch (UriFormatException)
                            {
                                if (!newLink.ToLower().StartsWith("http://"))
                                {
                                    newLink = "http://" + newLink;
                                    Uri reqUri = new Uri(newLink);
                                    newLink = reqUri.CanonicalizedUri();
                                }
                            }
                            f.link = newLink;
                            f = feedHandler.AddFeed(f);
                            tn.DataKey = f.link;
                            refreshThisFeed = true;
                        }
                        if (!f.title.Equals(propertiesDialog.textBox1.Text.Trim()))
                        {
                            f.title = propertiesDialog.textBox1.Text.Trim();
                            changes |= NewsFeedProperty.FeedTitle;
                            tn.Text = f.title;
                        }
                    }
                    try
                    {
                        if ((!string.IsNullOrEmpty(propertiesDialog.comboBox1.Text.Trim())))
                        {
                            Int32 intIn = Int32.Parse(propertiesDialog.comboBox1.Text.Trim());
                            changes |= NewsFeedProperty.FeedRefreshRate;
                            if (intIn <= 0)
                            {
                                DisableFeed(f, tn);
                                feedDisabled = true;
                            }
                            else
                            {
                                intIn = intIn*MilliSecsMultiplier;
                                feedHandler.SetRefreshRate(f.link, intIn);
                            }
                        }
                    }
                    catch (FormatException)
                    {
                        MessageError(SR.FormatExceptionRefreshRate);
                    }
                    catch (OverflowException)
                    {
                        MessageError(SR.OverflowExceptionRefreshRate);
                    }
                    string category = null;
                    if ((propertiesDialog.comboBox2.Text != null) &&
                        (!propertiesDialog.comboBox2.Text.Equals(String.Empty)) &&
                        (!propertiesDialog.comboBox2.Text.Equals(defaultCategory)))
                    {
                        category = propertiesDialog.comboBox2.Text.Trim();
                    }
                    if (category != null && !category.Equals(f.category))
                    {
                        changes |= NewsFeedProperty.FeedCategory;
                        if (!feedHandler.HasCategory(category))
                        {
                            feedHandler.AddCategory(category);
                        }
                        feedHandler.ChangeCategory(f, feedHandler.GetCategories()[category]);
                        TreeFeedsNodeBase target =
                            guiMain.CreateSubscriptionsCategoryHive(guiMain.GetRoot(RootFolderType.MyFeeds), category);
                        guiMain.MoveNode(tn, target, true);
                    }
                    if (propertiesDialog.comboMaxItemAge.Enabled)
                    {
                        if (feedMaxItemAge.CompareTo(propertiesDialog.MaxItemAge) != 0)
                        {
                            refreshThisFeed = true;
                            feedHandler.SetMaxItemAge(f.link, propertiesDialog.MaxItemAge);
                            changes |= NewsFeedProperty.FeedMaxItemAge;
                        }
                    }
                    if (propertiesDialog.textUser.Text != null && propertiesDialog.textUser.Text.Trim().Length != 0)
                    {
                        string u = propertiesDialog.textUser.Text.Trim(), p = null;
                        if (!string.IsNullOrEmpty(propertiesDialog.textPwd.Text))
                            p = propertiesDialog.textPwd.Text.Trim();
                        FeedSource.SetFeedCredentials(f, u, p);
                        changes |= NewsFeedProperty.FeedCredentials;
                        refreshThisFeed = true;
                    }
                    else
                    {
                        FeedSource.SetFeedCredentials(f, null, null);
                        changes |= NewsFeedProperty.FeedCredentials;
                    }
                    if (f.alertEnabled != propertiesDialog.checkEnableAlerts.Checked)
                        changes |= NewsFeedProperty.FeedAlertOnNewItemsReceived;
                    f.alertEnabledSpecified = f.alertEnabled = propertiesDialog.checkEnableAlerts.Checked;
                    if (propertiesDialog.checkMarkItemsReadOnExit.Checked != feedHandler.GetMarkItemsReadOnExit(f.link))
                    {
                        feedHandler.SetMarkItemsReadOnExit(f.link,
                                                           propertiesDialog.checkMarkItemsReadOnExit.Checked);
                        changes |= NewsFeedProperty.FeedMarkItemsReadOnExit;
                    }
                    if (refreshThisFeed && !feedDisabled)
                    {
                        feedHandler.MarkForDownload(f);
                    }
                    if (feedHandler.GetDownloadEnclosures(f.link) != propertiesDialog.checkDownloadEnclosures.Checked)
                    {
                        feedHandler.SetDownloadEnclosures(f.link, propertiesDialog.checkDownloadEnclosures.Checked);
                    }
                    if (feedHandler.GetEnclosureAlert(f.link) != propertiesDialog.checkEnableEnclosureAlerts.Checked)
                    {
                        feedHandler.SetEnclosureAlert(f.link, propertiesDialog.checkEnableEnclosureAlerts.Checked);
                    }
                    if (propertiesDialog.checkCustomFormatter.Checked)
                    {
                        string stylesheet = propertiesDialog.comboFormatters.Text;
                        if (!stylesheet.Equals(feedHandler.GetStyleSheet(f.link)))
                        {
                            feedHandler.SetStyleSheet(f.link, stylesheet);
                            changes |= NewsFeedProperty.FeedStylesheet;
                            if (!NewsItemFormatter.ContainsXslStyleSheet(stylesheet))
                            {
                                NewsItemFormatter.AddXslStyleSheet(stylesheet,
                                                                   GetNewsItemFormatterTemplate(stylesheet));
                            }
                        }
                    }
                    else
                    {
                        if (!String.Empty.Equals(feedHandler.GetStyleSheet(f.link)))
                        {
                            feedHandler.SetStyleSheet(f.link, String.Empty);
                            changes |= NewsFeedProperty.FeedStylesheet;
                        }
                    }
                    guiMain.SetSubscriptionNodeState(f, tn, FeedProcessingState.Normal);
                    FeedWasModified(f, changes);
                }
                propertiesDialog.Dispose();
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdShowCategoryProperties(ICommand sender)
        {
            if (guiMain.CurrentSelectedFeedsNode != null &&
                (guiMain.CurrentSelectedFeedsNode.Type == FeedNodeType.Category))
            {
                TreeFeedsNodeBase tn = guiMain.CurrentSelectedFeedsNode;
                string category = null, catPlusSep, categoryName;
                int refreshrate = Preferences.RefreshRate;
                TimeSpan feedMaxItemAge = TimeSpan.Zero;
                bool feedMarkItemsReadOnExit = false;
                try
                {
                    category = tn.CategoryStoreName;
                    catPlusSep = category + FeedSource.CategorySeparator;
                    categoryName = tn.Text;
                    try
                    {
                        refreshrate = feedHandler.GetCategoryRefreshRate(category);
                        feedMaxItemAge = feedHandler.GetCategoryMaxItemAge(category);
                        feedMarkItemsReadOnExit = feedHandler.GetCategoryMarkItemsReadOnExit(category);
                    }
                    catch
                    {
                    }
                }
                catch (Exception e)
                {
                    MessageError(SR.GUIStatusErrorGeneralFeedMessage(category, e.Message));
                    return;
                }
                CategoryProperties propertiesDialog =
                    new CategoryProperties(tn.Text, refreshrate/MilliSecsMultiplier, feedMaxItemAge,
                                           feedHandler.GetCategoryStyleSheet(category));
                propertiesDialog.comboMaxItemAge.Enabled = !feedMaxItemAge.Equals(TimeSpan.Zero);
                propertiesDialog.checkMarkItemsReadOnExit.Checked = feedMarkItemsReadOnExit;
                propertiesDialog.checkDownloadEnclosures.Checked =
                    feedHandler.GetCategoryDownloadEnclosures(category);
                propertiesDialog.checkEnableEnclosureAlerts.Checked =
                    feedHandler.GetCategoryEnclosureAlert(category);
                propertiesDialog.ShowDialog(guiMain);
                if (propertiesDialog.DialogResult == DialogResult.OK)
                {
                    NewsFeedProperty changes = NewsFeedProperty.General;
                    if ((propertiesDialog.textBox2.Text == null) ||
                        propertiesDialog.textBox2.Text.Trim().Equals(String.Empty))
                    {
                        MessageError(SR.GUIFieldTitleInvalid);
                    }
                    else
                    {
                        if (!categoryName.Equals(propertiesDialog.textBox2.Text.Trim()))
                        {
                            categoryName = propertiesDialog.textBox2.Text.Trim();
                            guiMain.RenameTreeNode(tn, categoryName);
                            feedHandler.RenameCategory(category, tn.CategoryStoreName);
                        }
                    }
                    try
                    {
                        if ((!string.IsNullOrEmpty(propertiesDialog.comboBox1.Text.Trim())))
                        {
                            Int32 intIn = Int32.Parse(propertiesDialog.comboBox1.Text.Trim());
                            if (intIn <= 0)
                            {
                                foreach (NewsFeed f in feedHandler.GetFeeds().Values)
                                {
                                    if ((f.category != null) &&
                                        (f.category.Equals(category) || f.category.StartsWith(catPlusSep)))
                                    {
                                        f.refreshrateSpecified = false;
                                        DisableFeed(f.link);
                                    }
                                }
                                feedHandler.SetCategoryRefreshRate(category, 0);
                            }
                            else
                            {
                                foreach (NewsFeed f in feedHandler.GetFeeds().Values)
                                {
                                    if ((f.category != null) &&
                                        (f.category.Equals(category) || f.category.StartsWith(catPlusSep)))
                                    {
                                        f.refreshrateSpecified = false;
                                        guiMain.SetSubscriptionNodeState(f, TreeHelper.FindNode(tn, f),
                                                                         FeedProcessingState.Normal);
                                    }
                                }
                                intIn = intIn*MilliSecsMultiplier;
                                feedHandler.SetCategoryRefreshRate(category, intIn);
                            }
                        }
                    }
                    catch (FormatException)
                    {
                        MessageError(SR.FormatExceptionRefreshRate);
                    }
                    catch (OverflowException)
                    {
                        MessageError(SR.OverflowExceptionRefreshRate);
                    }
                    if (propertiesDialog.comboMaxItemAge.Enabled)
                    {
                        if (feedMaxItemAge.CompareTo(propertiesDialog.MaxItemAge) != 0)
                        {
                            foreach (NewsFeed f in feedHandler.GetFeeds().Values)
                            {
                                if ((f.category != null) &&
                                    (f.category.Equals(category) || f.category.StartsWith(catPlusSep)))
                                {
                                    f.maxitemage = null;
                                }
                            }
                            feedHandler.SetCategoryMaxItemAge(category, propertiesDialog.MaxItemAge);
                            changes |= NewsFeedProperty.General;
                        }
                    }
                    feedHandler.SetCategoryMarkItemsReadOnExit(category,
                                                               propertiesDialog.checkMarkItemsReadOnExit.Checked);
                    feedHandler.SetCategoryDownloadEnclosures(category,
                                                              propertiesDialog.checkDownloadEnclosures.Checked);
                    feedHandler.SetCategoryEnclosureAlert(category,
                                                          propertiesDialog.checkEnableEnclosureAlerts.Checked);
                    if (propertiesDialog.checkCustomFormatter.Checked)
                    {
                        string stylesheet = propertiesDialog.comboFormatters.Text;
                        feedHandler.SetCategoryStyleSheet(category, stylesheet);
                        if (!NewsItemFormatter.ContainsXslStyleSheet(stylesheet))
                        {
                            NewsItemFormatter.AddXslStyleSheet(stylesheet,
                                                               GetNewsItemFormatterTemplate(stylesheet));
                        }
                    }
                    else
                    {
                        feedHandler.SetCategoryStyleSheet(category, String.Empty);
                    }
                    SubscriptionModified(changes);
                }
                propertiesDialog.Dispose();
            }
            if (sender is AppContextMenuCommand)
                guiMain.CurrentSelectedFeedsNode = null;
        }
        public void CmdMarkFeedItemsUnread(ICommand sender)
        {
            guiMain.MarkSelectedItemsLVUnread();
        }
        public void CmdMarkFeedItemsRead(ICommand sender)
        {
            guiMain.MarkSelectedItemsLVRead();
        }
        public void CmdPostReplyToItem(ICommand sender)
        {
            INewsItem item2reply = guiMain.CurrentSelectedFeedItem;
            if (item2reply == null)
            {
                MessageInfo(SR.GuiStateNoFeedItemSelectedMessage);
                return;
            }
            if ((postReplyForm == null) || (postReplyForm.IsDisposed))
            {
                postReplyForm = new PostReplyForm(Preferences.UserIdentityForComments, IdentityManager);
                postReplyForm.PostReply += OnPostReplyFormPostReply;
            }
            postReplyForm.ReplyToItem = item2reply;
            postReplyForm.Show();
            Win32.SetForegroundWindow(postReplyForm.Handle);
        }
        public void CmdPostNewItem(ICommand sender)
        {
            TreeFeedsNodeBase tn = guiMain.CurrentSelectedFeedsNode;
            if (tn == null || tn.Type != FeedNodeType.Feed)
            {
                Mediator.SetEnabled("-cmdFeedItemNewPost");
                return;
            }
            string feedUrl = tn.DataKey;
            if (feedUrl == null ||
                !RssHelper.IsNntpUrl(feedUrl) ||
                !FeedHandler.IsSubscribed(feedUrl))
            {
                Mediator.SetEnabled("-cmdFeedItemNewPost");
                return;
            }
            if ((postReplyForm == null) || (postReplyForm.IsDisposed))
            {
                postReplyForm = new PostReplyForm(Preferences.UserIdentityForComments, IdentityManager);
                postReplyForm.PostReply += OnPostReplyFormPostReply;
            }
            INewsFeed f = null;
            if (FeedHandler.GetFeeds().TryGetValue(feedUrl, out f)) {
                postReplyForm.PostToFeed = f;
                postReplyForm.Show();
                Win32.SetForegroundWindow(postReplyForm.Handle);
            }
        }
        public void CmdBrowserGoBack(ICommand sender)
        {
            guiMain.RequestBrowseAction(BrowseAction.NavigateBack);
        }
        public void CmdBrowserGoForward(ICommand sender)
        {
            guiMain.RequestBrowseAction(BrowseAction.NavigateForward);
        }
        public void CmdBrowserCancelNavigation(ICommand sender)
        {
            guiMain.RequestBrowseAction(BrowseAction.NavigateCancel);
        }
        public void CmdBrowserNavigate(ICommand sender)
        {
            NavigateToUrl(guiMain.UrlText, "Web", (Control.ModifierKeys & Keys.Control) == Keys.Control, true);
        }
        public void CmdBrowserRefresh(ICommand sender)
        {
            guiMain.RequestBrowseAction(BrowseAction.DoRefresh);
        }
        public void CmdBrowserCreateNewTab(ICommand sender)
        {
            NavigateToUrl("about:blank", "New Browser", true, true);
        }
        public void CmdGenericListviewCommand(ICommand sender)
        {
            AppContextMenuCommand cmd = (AppContextMenuCommand) sender;
            string s = (string) cmd.Tag;
            guiMain.OnGenericListviewCommand(Int32.Parse(s.Substring(s.IndexOf(".") + 1)), false);
        }
        public void CmdGenericListviewCommandConfig(ICommand sender)
        {
            AppContextMenuCommand cmd = (AppContextMenuCommand) sender;
            string s = (string) cmd.Tag;
            guiMain.OnGenericListviewCommand(Int32.Parse(s.Substring(s.IndexOf(".") + 1)), true);
        }
    }
}
