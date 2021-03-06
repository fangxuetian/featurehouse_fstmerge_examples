using System;
using System.Xml.Serialization;
namespace ThoughtWorks.CruiseControl.Remote
{
    [Serializable]
    [XmlRoot("message")]
    public class Message
    {
        public enum MessageKind
        {
            NotDefined = 0,
            Breakers = 1,
            Fixer = 2,
            FailingTasks = 3,
            BuildStatus = 4
        }
        private string message;
        private MessageKind messageKind;
        public Message()
        { }
        public Message(string message)
        {
            this.message = message;
            this.Kind = MessageKind.NotDefined;
        }
        public Message(string message, MessageKind kind)
        {
            this.message = message;
            this.Kind = kind;
        }
        [XmlText]
        public string Text
        {
            get { return message; }
            set { message = value; }
        }
        [XmlAttribute]
        public MessageKind Kind
        {
            get { return messageKind; }
            set { messageKind = value; }
        }
        public override string ToString()
        {
            return message;
        }
        public override bool Equals(object obj)
        {
            var m = obj as Message;
            if (m == null)
            {
                return false;
            }
            return string.Equals(this.Text, m.Text) && (this.Kind == m.Kind);
        }
        public override int GetHashCode()
        {
            return this.ToString().GetHashCode();
        }
    }
}
