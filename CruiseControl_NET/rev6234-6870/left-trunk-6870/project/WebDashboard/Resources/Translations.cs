namespace ThoughtWorks.CruiseControl.WebDashboard.Resources
{
    using System;
    using System.Globalization;
    using System.Resources;
    using System.Web;
    public class Translations
    {
        private CultureInfo culture;
        private ResourceManager resourceManager = new ResourceManager("ThoughtWorks.CruiseControl.WebDashboard.Resources.Languages",
            typeof(Translations).Assembly);
        private Translations()
        {
            var context = HttpContext.Current;
            try
            {
                if ((context != null) &&
                    (context.Request != null) &&
                    (context.Request.UserLanguages.Length > 0))
                {
                    foreach (var language in context.Request.UserLanguages)
                    {
                        try
                        {
                            this.culture = new CultureInfo(language);
                            if (!this.culture.IsNeutralCulture)
                            {
                                break;
                            }
                        }
                        catch (ArgumentException)
                        {
                        }
                        if (this.culture.IsNeutralCulture)
                        {
                            this.culture = new CultureInfo(context.Request.UserLanguages[0]);
                        }
                    }
                }
                else
                {
                    this.culture = CultureInfo.CurrentUICulture;
                }
            }
            catch
            {
                this.culture = CultureInfo.CurrentUICulture;
            }
            this.resourceManager.IgnoreCase = true;
        }
        public Translations(string culture)
        {
            this.culture = new CultureInfo(culture);
            this.resourceManager.IgnoreCase = true;
        }
        public CultureInfo Culture
        {
            get { return this.culture; }
        }
        public CultureInfo UICulture
        {
            get
            {
                if (this.culture.IsNeutralCulture)
                {
                    return CultureInfo.CurrentUICulture;
                }
                else
                {
                    return this.culture;
                }
            }
        }
        public static Translations RetrieveCurrent()
        {
            var context = HttpContext.Current;
            if ((context == null) || (context.Items == null))
            {
                return new Translations();
            }
            else
            {
                if (context.Items.Contains("translations"))
                {
                    return context.Items["translations"] as Translations;
                }
                else
                {
                    var value = new Translations();
                    context.Items.Add("translations", value);
                    return value;
                }
            }
        }
        public string Translate(string value, params object[] args)
        {
            var translation = this.resourceManager.GetString(value, this.culture) ?? value;
            if (args.Length > 0)
            {
                translation = string.Format(this.culture, translation, args);
            }
            return translation;
        }
        public string TranslateJson(string value, params object[] args)
        {
            var translation = this.resourceManager.GetString(value, this.culture) ?? value;
            if (args.Length > 0)
            {
                translation = string.Format(this.culture, translation, args);
            }
            return translation.Replace("'", "\\'").Replace("\"", "\\\"");
        }
    }
}
