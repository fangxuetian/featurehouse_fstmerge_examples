using System;
using System.Collections.Generic;
using System.Xml;
using System.Xml.Linq;
namespace ThoughtWorks.CruiseControl.Core.Config.Preprocessor.ElementProcessors
{
    internal class DefaultProcessor : ElementProcessor
    {
        private readonly ExpandSymbolProcessor _expand_symbol_processor;
        private readonly Dictionary< XName, IElementProcessor > _processors;
        private IEnumerable< XNode > _emptyNodeSet = new XNode[] {};
        public DefaultProcessor(PreprocessorEnvironment env) : base( env._Settings.Namespace.GetName("define"), env )
        {
            _processors = new Dictionary< XName, IElementProcessor >();
            _expand_symbol_processor = new ExpandSymbolProcessor( env );
            _LoadElementProcessors();
        }
        public override IEnumerable< XNode > Process(XNode node)
        {
            try
            {
                switch ( node.NodeType )
                {
                    case XmlNodeType.Element:
                        return _ProcessElement( ( XElement ) node );
                    case XmlNodeType.ProcessingInstruction:
                        return new[] {new XProcessingInstruction( ( XProcessingInstruction ) node )};
                    case XmlNodeType.CDATA:
                        return new[] {new XCData( ( XCData ) node )};
                    case XmlNodeType.Text:
                        return _ProcessTextNode( ( XText ) node );
                    case XmlNodeType.Comment:
                        return new[] {new XComment( ( XComment ) node )};
                    default:
                        throw new InvalidOperationException( "Unhandled Xml Node Type: " +
                                                             node.NodeType );
                }
            }
            catch ( PreprocessorException )
            {
                throw;
            }
        }
        private IEnumerable< XNode > _ProcessElement(XElement element)
        {
            return element.Name.Namespace == XmlNs.PreProcessor
                       ? _ProcessPpElement( element )
                       : _ProcessNonPpElement( element );
        }
        protected IEnumerable< XNode > _ProcessTextNode(XText node)
        {
            if (_Env._Settings.IgnoreWhitespace && node.Value.Trim().Length == 0)
                return _emptyNodeSet;
            return _Env.EvalTextSymbols( node.Value );
        }
        protected IEnumerable< XNode > _ProcessNonPpElement(XElement element)
        {
            var copy = new XElement( element.Name );
            foreach ( XAttribute attr in element.Attributes() )
            {
                copy.Add( new XAttribute( attr.Name,
                                          _ProcessText( attr.Value ).GetTextValue().Trim() ) );
            }
            foreach ( XNode node in element.Nodes() )
            {
                IEnumerable< XNode > add_node = Process( node );
                if ( add_node != null )
                    copy.Add( add_node );
            }
            return new[] {copy};
        }
        protected IEnumerable< XNode > _ProcessPpElement(XElement element)
        {
            IElementProcessor processor;
            if ( _processors.TryGetValue( element.Name, out processor ) )
            {
                return processor.Process( element );
            }
            return _expand_symbol_processor.Process( element );
        }
        private void _LoadElementProcessors()
        {
            _RegisterElementProcessor( new ConfigTemplateProcessor( _Env ) );
            _RegisterElementProcessor( new DefineProcessor( _Env ) );
            _RegisterElementProcessor( new EvalProcessor( _Env ) );
            _RegisterElementProcessor( new IncludeProcessor( _Env ) );
            _RegisterElementProcessor( new CountProcessor( _Env ) );
            _RegisterElementProcessor( new ScopeProcessor( _Env ) );
            _RegisterElementProcessor( new IfProcessor( _Env ) );
            _RegisterElementProcessor( new IfDefProcessor( _Env ) );
            _RegisterElementProcessor( new IfNDefProcessor( _Env ) );
            _RegisterElementProcessor( new ImportProcessor( _Env ) );
            _RegisterElementProcessor( new CreateElementProcessor( _Env ) );
            _RegisterElementProcessor( new IgnoreProcessor( _Env._Settings.Namespace.GetName("else"), _Env ) );
            _RegisterElementProcessor( new ProcessingInstructionProcessor( _Env ) );
            _RegisterElementProcessor( new ForProcessor( _Env ) );
            _RegisterElementProcessor( new ForEachProcessor( _Env ) );
        }
        internal void _RegisterElementProcessor(IElementProcessor processor)
        {
            _processors[ processor.TargetElementName ] = processor;
        }
    }
}
