using System;
using System.Collections.Generic;
namespace NewsComponents.RelationCosmos
{
    internal class DummyCosmos : IRelationCosmos
    {
        public void Add<T>(T relation) where T : IRelation
        {
        }
        public void AddRange<T>(IEnumerable<T> relations)
            where T : IRelation
        {
        }
        public void Remove<T>(T relation) where T : IRelation
        {
        }
        public void RemoveRange<T>(IEnumerable<T> relations)
            where T : IRelation
        {
        }
        public void Clear()
        {
        }
        private bool _deepCosmos;
        public bool DeepCosmos
        {
            get
            {
                return _deepCosmos;
            }
            set
            {
                _deepCosmos = value;
            }
        }
        private bool _adjustTime;
        public bool AdjustPointInTime
        {
            get
            {
                return _adjustTime;
            }
            set
            {
                _adjustTime = value;
            }
        }
        public IList<T> GetIncoming<T>(T relation, IList<T> excludeRelations) where T : IRelation
        {
            return new List<T>();
        }
        public IList<T> GetIncoming<T>(string hRef, DateTime since) where T : IRelation {
            return new List<T>();
        }
        public IList<T> GetOutgoing<T>(T relation, IList<T> excludeRelations) where T : IRelation
        {
            return new List<T>();
        }
        public IList<T> GetIncomingAndOutgoing<T>(T relation, IList<T> excludeRelations) where T : IRelation
        {
            return new List<T>();
        }
        public bool HasIncomingOrOutgoing<T>(T relation, IList<T> excludeRelations) where T : IRelation
        {
            return false;
        }
    }
}
