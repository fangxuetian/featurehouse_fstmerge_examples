using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using ProcessHacker.Common;
namespace ProcessHacker.UI
{
    public enum ListViewItemState
    {
        Normal, New, Removed
    }
    public class HighlightingContext : IDisposable
    {
        public static event MethodInvoker HighlightingDurationChanged;
        private static Dictionary<ListViewItemState, Color> _colors = new Dictionary<ListViewItemState, Color>();
        private static int _highlightingDuration = 1000;
        private static bool _stateHighlighting = true;
        static HighlightingContext()
        {
            _colors.Add(ListViewItemState.New, Color.FromArgb(0xe0f0e0));
            _colors.Add(ListViewItemState.Removed, Color.FromArgb(0xf0e0e0));
        }
        public static Dictionary<ListViewItemState, Color> Colors
        {
            get { return _colors; }
        }
        public static int HighlightingDuration
        {
            get { return _highlightingDuration; }
            set
            {
                _highlightingDuration = value;
                if (HighlightingDurationChanged != null)
                    HighlightingDurationChanged();
            }
        }
        public static bool StateHighlighting
        {
            get { return _stateHighlighting; }
            set { _stateHighlighting = value; }
        }
        private ListView _list;
        private Queue<MethodInvoker> _preQueue = new Queue<MethodInvoker>();
        private Queue<MethodInvoker> _queue = new Queue<MethodInvoker>();
        public HighlightingContext(ListView list)
        {
            _list = list;
        }
        public void Tick()
        {
            if (!_list.IsHandleCreated)
                return;
            _list.BeginInvoke(new MethodInvoker(delegate
            {
                _list.BeginUpdate();
                while (_preQueue.Count > 0)
                    _preQueue.Dequeue().Invoke();
                _list.EndUpdate();
                System.Threading.Timer t = null;
                t = new System.Threading.Timer(o =>
                {
                    if (_list.IsHandleCreated)
                    {
                        _list.BeginInvoke(new MethodInvoker(delegate
                        {
                            _list.BeginUpdate();
                            while (_queue.Count > 0)
                                _queue.Dequeue().Invoke();
                            _list.EndUpdate();
                        }));
                    }
                    t.Dispose();
                }, null, HighlightingContext.HighlightingDuration, System.Threading.Timeout.Infinite);
            }));
        }
        public void Enqueue(MethodInvoker method)
        {
            _queue.Enqueue(method);
        }
        public void EnqueuePre(MethodInvoker method)
        {
            _preQueue.Enqueue(method);
        }
        public void Dispose()
        {
        }
    }
    public class HighlightedListViewItem : ListViewItem
    {
        private HighlightingContext _context;
        private Color _normalColor = SystemColors.Window;
        private ListViewItemState _state = ListViewItemState.Normal;
        public HighlightedListViewItem(HighlightingContext context)
            : this(context, true)
        { }
        public HighlightedListViewItem(HighlightingContext context, bool highlight)
            : this(context, "", highlight)
        { }
        public HighlightedListViewItem(HighlightingContext context, string text)
            : this(context, text, true)
        { }
        public HighlightedListViewItem(HighlightingContext context, string text, bool highlight)
            : base(text)
        {
            _context = context;
            if (HighlightingContext.StateHighlighting && highlight)
            {
                this.BackColor = HighlightingContext.Colors[ListViewItemState.New];
                this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                _state = ListViewItemState.New;
                _context.Enqueue(delegate
                    {
                        this.BackColor = _normalColor;
                        this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                        _state = ListViewItemState.Normal;
                    });
            }
            else
            {
                this.BackColor = _normalColor;
            }
        }
        public override void Remove()
        {
            if (HighlightingContext.StateHighlighting)
            {
                _context.EnqueuePre(delegate
                    {
                        this.BackColor = HighlightingContext.Colors[ListViewItemState.Removed];
                        this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                        _context.Enqueue(delegate
                        {
                            this.BaseRemove();
                        });
                    });
            }
            else
            {
                base.Remove();
            }
        }
        private void BaseRemove()
        {
            base.Remove();
        }
        public Color NormalColor
        {
            get { return _normalColor; }
            set
            {
                _normalColor = value;
                if (_state == ListViewItemState.Normal)
                {
                    this.BackColor = value;
                    this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                }
            }
        }
        public void SetTemporaryState(ListViewItemState state)
        {
            _context.EnqueuePre(delegate
            {
                this.BackColor = HighlightingContext.Colors[state];
                this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                _state = state;
                _context.Enqueue(delegate
                {
                    this.BackColor = _normalColor;
                    this.ForeColor = PhUtils.GetForeColor(this.BackColor);
                    _state = ListViewItemState.Normal;
                });
            });
        }
    }
}
