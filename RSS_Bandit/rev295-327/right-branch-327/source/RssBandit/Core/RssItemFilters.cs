using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Forms.ThListView;
using NewsComponents;
using RssBandit.AppServices;
using RssBandit.WinGui.Interfaces;
using RssBandit.WinGui.Utility;
namespace RssBandit.Filter
{
    internal class NewsItemFilterManager
    {
        public event FilterActionCancelEventHandler FilterMatch;
        public delegate void FilterActionCancelEventHandler(object sender, FilterActionCancelEventArgs e);
        private readonly Dictionary<string, INewsItemFilter> filters = new Dictionary<string, INewsItemFilter>();
        public INewsItemFilter Add(string key, INewsItemFilter newFilter)
        {
            if (key == null || newFilter == null)
                throw new ArgumentException("Parameter cannot be null", (newFilter == null ? "key" : "newFilter"));
            if (filters.ContainsKey(key))
                filters.Remove(key);
            filters.Add(key, newFilter);
            return newFilter;
        }
        public INewsItemFilter this[string key]
        {
            get
            {
                return filters[key];
            }
            set
            {
                filters[key] = value;
            }
        }
        public void Remove(string key)
        {
            if (filters.ContainsKey(key))
                filters.Remove(key);
        }
        public bool Apply( ThreadedListViewItem lvItem)
        {
            NewsItem item = lvItem.Key as NewsItem;
            if (item == null)
                return false;
            bool anyApplied = false;
            foreach (string key in filters.Keys)
            {
                INewsItemFilter filter = filters[key];
                if (filter != null && filter.Match(item))
                {
                    if (!CancelFilterAction(key))
                    {
                        filter.ApplyAction(item, lvItem);
                        anyApplied = true;
                    }
                }
            }
            return anyApplied;
        }
        public bool Apply(string key, NewsItem item)
        {
            bool anyApplied = false;
            if (!filters.ContainsKey(key))
                return anyApplied;
            INewsItemFilter filter = filters[key];
            if (filter.Match(item))
            {
                if (!CancelFilterAction(key))
                {
                    filter.ApplyAction(item, null);
                    anyApplied = true;
                }
            }
            return anyApplied;
        }
        protected bool CancelFilterAction(string key)
        {
            if (FilterMatch != null)
            {
                FilterActionCancelEventArgs ceh = new FilterActionCancelEventArgs(key, false);
                FilterMatch(this, ceh);
                return ceh.Cancel;
            }
            return false;
        }
        public class FilterActionCancelEventArgs : CancelEventArgs
        {
            private readonly string filterKey = null;
            public FilterActionCancelEventArgs()
            {
                ;
            }
            public FilterActionCancelEventArgs(string key, bool cancelState) : base(cancelState)
            {
                filterKey = key;
            }
            public string FilterKey
            {
                get
                {
                    return filterKey;
                }
            }
        }
    }
    internal class NewsItemReferrerFilter : INewsItemFilter
    {
        private string _referrer;
        public NewsItemReferrerFilter(ICoreApplication app)
        {
            _referrer = null;
            app.PreferencesChanged += OnPreferencesChanged;
            app.FeedlistLoaded += OnFeedlistLoaded;
        }
        private void OnFeedlistLoaded(object sender, EventArgs e)
        {
            RssBanditApplication app = sender as RssBanditApplication;
            if (app != null)
            {
                if (app.FeedHandler.UserIdentity != null &&
                    app.FeedHandler.UserIdentity.ContainsKey(app.Preferences.UserIdentityForComments))
                    InitWith(app.FeedHandler.UserIdentity[app.Preferences.UserIdentityForComments]);
            }
        }
        private void OnPreferencesChanged(object sender, EventArgs e)
        {
            RssBanditApplication app = (RssBanditApplication) sender;
            if (app != null)
            {
                if (app.FeedHandler.UserIdentity != null &&
                    app.FeedHandler.UserIdentity.ContainsKey(app.Preferences.UserIdentityForComments))
                    InitWith(app.FeedHandler.UserIdentity[app.Preferences.UserIdentityForComments]);
            }
        }
        private void InitWith(IUserIdentity ui)
        {
            if (ui != null && ui.ReferrerUrl != null && ui.ReferrerUrl.Length > 0)
                _referrer = ui.ReferrerUrl;
        }
        public bool Match(NewsItem item)
        {
            if (_referrer != null && item != null)
            {
                if ((item.HasContent) && (item.Content.IndexOf(_referrer) >= 0))
                {
                    return true;
                }
            }
            return false;
        }
        public void ApplyAction(NewsItem item, ThreadedListViewItem lvItem)
        {
            if (lvItem != null)
            {
                lvItem.Font = FontColorHelper.MergeFontStyles(lvItem.Font, FontColorHelper.ReferenceStyle);
                lvItem.ForeColor = FontColorHelper.ReferenceColor;
            }
        }
    }
    internal class NewsItemFlagFilter : INewsItemFilter
    {
        public bool Match(NewsItem item)
        {
            if (item != null && item.FlagStatus != Flagged.None)
                return true;
            return false;
        }
        public void ApplyAction(NewsItem item, ThreadedListViewItem lvItem)
        {
            if (lvItem != null && item != null)
            {
                lvItem.Font = FontColorHelper.MergeFontStyles(lvItem.Font, FontColorHelper.HighlightStyle);
                lvItem.ForeColor = FontColorHelper.HighlightColor;
            }
        }
    }
}
