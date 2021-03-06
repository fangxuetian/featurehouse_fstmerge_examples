


package net.sourceforge.pmd.jsp.ast;

public class JspParser implements JspParserTreeConstants, JspParserConstants {
  protected JJTJspParserState jjtree = new JJTJspParserState();
        
        private static String quoteContent(String quote) {
                return quote.substring(1, quote.length()-1);
        }

        
        private static String expressionContent(String expression) {
                return expression.substring(2, expression.length()-1).trim();
        }






  final public ASTCompilationUnit CompilationUnit() throws ParseException {
 
  ASTCompilationUnit jjtn000 = new ASTCompilationUnit(this, JJTCOMPILATIONUNIT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      Prolog();
      Content();
      jj_consume_token(0);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
    throw new RuntimeException("Missing return statement in function");
  }


  final public void Prolog() throws ParseException {
    if (jj_2_1(2147483647)) {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT_START:
        case JSP_COMMENT_START:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT_START:
          CommentTag();
          break;
        case JSP_COMMENT_START:
          JspComment();
          break;
        default:
          jj_la1[1] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      Declaration();
    } else {
      ;
    }
    if (jj_2_2(2147483647)) {
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT_START:
        case JSP_COMMENT_START:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT_START:
          CommentTag();
          break;
        case JSP_COMMENT_START:
          JspComment();
          break;
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      DoctypeDeclaration();
    } else {
      ;
    }
  }


  final public void Content() throws ParseException {
 
  ASTContent jjtn000 = new ASTContent(this, JJTCONTENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EL_EXPRESSION:
      case UNPARSED_TEXT:
        Text();
        break;
      case TAG_START:
      case COMMENT_START:
      case CDATA_START:
      case JSP_COMMENT_START:
      case JSP_DECLARATION_START:
      case JSP_EXPRESSION_START:
      case JSP_SCRIPTLET_START:
      case JSP_DIRECTIVE_START:
        ContentElementPossiblyWithText();
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TAG_START:
        case COMMENT_START:
        case CDATA_START:
        case JSP_COMMENT_START:
        case JSP_DECLARATION_START:
        case JSP_EXPRESSION_START:
        case JSP_SCRIPTLET_START:
        case JSP_DIRECTIVE_START:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_3;
        }
        ContentElementPossiblyWithText();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }


  final public void ContentElementPossiblyWithText() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMENT_START:
      CommentTag();
      break;
    case TAG_START:
      Element();
      break;
    case CDATA_START:
      CData();
      break;
    case JSP_COMMENT_START:
      JspComment();
      break;
    case JSP_DECLARATION_START:
      JspDeclaration();
      break;
    case JSP_EXPRESSION_START:
      JspExpression();
      break;
    case JSP_SCRIPTLET_START:
      JspScriptlet();
      break;
    case JSP_DIRECTIVE_START:
      JspDirective();
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EL_EXPRESSION:
    case UNPARSED_TEXT:
      Text();
      break;
    default:
      jj_la1[7] = jj_gen;
      ;
    }
  }

  final public void JspDirective() throws ParseException {
 
        ASTJspDirective jjtn000 = new ASTJspDirective(this, JJTJSPDIRECTIVE);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(JSP_DIRECTIVE_START);
      t = jj_consume_token(JSP_DIRECTIVE_NAME);
                                   jjtn000.setName(t.image);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case JSP_DIRECTIVE_ATTRIBUTE_NAME:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_4;
        }
        JspDirectiveAttribute();
      }
      jj_consume_token(JSP_DIRECTIVE_END);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void JspDirectiveAttribute() throws ParseException {
 
        ASTJspDirectiveAttribute jjtn000 = new ASTJspDirectiveAttribute(this, JJTJSPDIRECTIVEATTRIBUTE);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(JSP_DIRECTIVE_ATTRIBUTE_NAME);
                                             jjtn000.setName(t.image);
      jj_consume_token(JSP_DIRECTIVE_ATTRIBUTE_EQUALS);
      t = jj_consume_token(JSP_DIRECTIVE_ATTRIBUTE_VALUE);
                                              jjtree.closeNodeScope(jjtn000, true);
                                              jjtc000 = false;
                                              jjtn000.setValue(quoteContent(t.image));
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void JspScriptlet() throws ParseException {
 
        ASTJspScriptlet jjtn000 = new ASTJspScriptlet(this, JJTJSPSCRIPTLET);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(JSP_SCRIPTLET_START);
      t = jj_consume_token(JSP_SCRIPTLET);
                              jjtn000.setImage(t.image.trim());
      jj_consume_token(JSP_SCRIPTLET_END);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void JspExpression() throws ParseException {
 
        ASTJspExpression jjtn000 = new ASTJspExpression(this, JJTJSPEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(JSP_EXPRESSION_START);
      t = jj_consume_token(JSP_EXPRESSION);
                               jjtn000.setImage(t.image.trim());
      jj_consume_token(JSP_EXPRESSION_END);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void JspDeclaration() throws ParseException {
 
        ASTJspDeclaration jjtn000 = new ASTJspDeclaration(this, JJTJSPDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(JSP_DECLARATION_START);
      t = jj_consume_token(JSP_DECLARATION);
                                jjtn000.setImage(t.image.trim());
      jj_consume_token(JSP_DECLARATION_END);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void JspComment() throws ParseException {
 
        ASTJspComment jjtn000 = new ASTJspComment(this, JJTJSPCOMMENT);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(JSP_COMMENT_START);
      t = jj_consume_token(JSP_COMMENT_CONTENT);
                                    jjtn000.setImage(t.image.trim());
      jj_consume_token(JSP_COMMENT_END);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }


  final public void Text() throws ParseException {
 
        ASTText jjtn000 = new ASTText(this, JJTTEXT);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);StringBuffer content = new StringBuffer();
        String tmp;
    try {
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case UNPARSED_TEXT:
          tmp = UnparsedText();
                                       content.append(tmp);
          break;
        case EL_EXPRESSION:
          tmp = ElExpression();
                                       content.append(tmp);
          break;
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EL_EXPRESSION:
        case UNPARSED_TEXT:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_5;
        }
      }
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtn000.setImage(content.toString());
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public String UnparsedText() throws ParseException {
 
  ASTUnparsedText jjtn000 = new ASTUnparsedText(this, JJTUNPARSEDTEXT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(UNPARSED_TEXT);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(t.image);
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }


  final public String UnparsedTextNoSingleQuotes() throws ParseException {
 
  ASTUnparsedText jjtn000 = new ASTUnparsedText(this, JJTUNPARSEDTEXT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(UNPARSED_TEXT_NO_SINGLE_QUOTES);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(t.image);
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }


  final public String UnparsedTextNoDoubleQuotes() throws ParseException {
 
  ASTUnparsedText jjtn000 = new ASTUnparsedText(this, JJTUNPARSEDTEXT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(UNPARSED_TEXT_NO_DOUBLE_QUOTES);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(t.image);
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }


  final public String ElExpression() throws ParseException {
 
        ASTElExpression jjtn000 = new ASTElExpression(this, JJTELEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(EL_EXPRESSION);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(expressionContent(t.image));
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }

  final public String ValueBindingInAttribute() throws ParseException {
 
        ASTValueBinding jjtn000 = new ASTValueBinding(this, JJTVALUEBINDING);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(VALUE_BINDING_IN_ATTRIBUTE);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(expressionContent(t.image));
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }

  final public String ElExpressionInAttribute() throws ParseException {
 
        ASTElExpression jjtn000 = new ASTElExpression(this, JJTELEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(EL_EXPRESSION_IN_ATTRIBUTE);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(expressionContent(t.image));
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }

  final public void CData() throws ParseException {
 
        ASTCData jjtn000 = new ASTCData(this, JJTCDATA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);StringBuffer content = new StringBuffer();
        Token t;
    try {
      jj_consume_token(CDATA_START);
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case UNPARSED:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_6;
        }
        t = jj_consume_token(UNPARSED);
                                          content.append(t.image);
      }
      jj_consume_token(CDATA_END);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(content.toString());
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }


  final public void Element() throws ParseException {
 
        ASTElement jjtn000 = new ASTElement(this, JJTELEMENT);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token startTagName;
        Token endTagName;
        String tagName;
    try {
      jj_consume_token(TAG_START);
      startTagName = jj_consume_token(TAG_NAME);
                                tagName = startTagName.image; jjtn000.setName(tagName);
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ATTR_NAME:
          ;
          break;
        default:
          jj_la1[12] = jj_gen;
          break label_7;
        }
        Attribute();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TAG_END:
        jj_consume_token(TAG_END);
                        jjtn000.setEmpty(false);

                        
                        
                        
                        
                        
                                if ("script".equalsIgnoreCase(startTagName.image)) {
                                        token_source.SwitchTo(HtmlScriptContentState);
                                }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TAG_START:
        case COMMENT_START:
        case CDATA_START:
        case JSP_COMMENT_START:
        case JSP_DECLARATION_START:
        case JSP_EXPRESSION_START:
        case JSP_SCRIPTLET_START:
        case JSP_DIRECTIVE_START:
        case EL_EXPRESSION:
        case UNPARSED_TEXT:
        case HTML_SCRIPT_CONTENT:
        case HTML_SCRIPT_END_TAG:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case HTML_SCRIPT_CONTENT:
          case HTML_SCRIPT_END_TAG:
            HtmlScript();
            break;
          case TAG_START:
          case COMMENT_START:
          case CDATA_START:
          case JSP_COMMENT_START:
          case JSP_DECLARATION_START:
          case JSP_EXPRESSION_START:
          case JSP_SCRIPTLET_START:
          case JSP_DIRECTIVE_START:
          case EL_EXPRESSION:
          case UNPARSED_TEXT:
            Content();
            break;
          default:
            jj_la1[13] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[14] = jj_gen;
          ;
        }
        jj_consume_token(ENDTAG_START);
        endTagName = jj_consume_token(TAG_NAME);
                        if (! tagName.equalsIgnoreCase(endTagName.image)) {
                                {if (true) throw new StartAndEndTagMismatchException(
                                        startTagName.beginLine, startTagName.beginColumn,
                                        startTagName.image,
                                        endTagName.beginLine, endTagName.beginColumn,
                                        endTagName.image  );}
                        }
        jj_consume_token(TAG_END);
        break;
      case TAG_SLASHEND:
        jj_consume_token(TAG_SLASHEND);
                          jjtree.closeNodeScope(jjtn000, true);
                          jjtc000 = false;
                          jjtn000.setEmpty(true);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void Attribute() throws ParseException {
 
        ASTAttribute jjtn000 = new ASTAttribute(this, JJTATTRIBUTE);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(ATTR_NAME);
                    jjtn000.setName(t.image);
      jj_consume_token(ATTR_EQ);
      AttributeValue();
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }


  final public void AttributeValue() throws ParseException {
 
        ASTAttributeValue jjtn000 = new ASTAttributeValue(this, JJTATTRIBUTEVALUE);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);StringBuffer content = new StringBuffer();
        String tmp;
        Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOUBLE_QUOTE:
        jj_consume_token(DOUBLE_QUOTE);
        label_8:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case EL_EXPRESSION_IN_ATTRIBUTE:
          case VALUE_BINDING_IN_ATTRIBUTE:
          case JSP_EXPRESSION_IN_ATTRIBUTE:
          case UNPARSED_TEXT_NO_DOUBLE_QUOTES:
            ;
            break;
          default:
            jj_la1[16] = jj_gen;
            break label_8;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case UNPARSED_TEXT_NO_DOUBLE_QUOTES:
            tmp = UnparsedTextNoDoubleQuotes();
            break;
          case EL_EXPRESSION_IN_ATTRIBUTE:
          case VALUE_BINDING_IN_ATTRIBUTE:
          case JSP_EXPRESSION_IN_ATTRIBUTE:
            tmp = QuoteIndependentAttributeValueContent();
            break;
          default:
            jj_la1[17] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
                            content.append(tmp);
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ENDING_DOUBLE_QUOTE:
          jj_consume_token(ENDING_DOUBLE_QUOTE);
          break;
        case DOLLAR_OR_HASH_DOUBLE_QUOTE:
          t = jj_consume_token(DOLLAR_OR_HASH_DOUBLE_QUOTE);
                                                                    content.append(t.image.substring(0, 1));
          break;
        default:
          jj_la1[18] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      case SINGLE_QUOTE:
        jj_consume_token(SINGLE_QUOTE);
        label_9:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case EL_EXPRESSION_IN_ATTRIBUTE:
          case VALUE_BINDING_IN_ATTRIBUTE:
          case JSP_EXPRESSION_IN_ATTRIBUTE:
          case UNPARSED_TEXT_NO_SINGLE_QUOTES:
            ;
            break;
          default:
            jj_la1[19] = jj_gen;
            break label_9;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case UNPARSED_TEXT_NO_SINGLE_QUOTES:
            tmp = UnparsedTextNoSingleQuotes();
            break;
          case EL_EXPRESSION_IN_ATTRIBUTE:
          case VALUE_BINDING_IN_ATTRIBUTE:
          case JSP_EXPRESSION_IN_ATTRIBUTE:
            tmp = QuoteIndependentAttributeValueContent();
            break;
          default:
            jj_la1[20] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
                            content.append(tmp);
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ENDING_SINGLE_QUOTE:
          jj_consume_token(ENDING_SINGLE_QUOTE);
          break;
        case DOLLAR_OR_HASH_SINGLE_QUOTE:
          t = jj_consume_token(DOLLAR_OR_HASH_SINGLE_QUOTE);
                                                                 content.append(t.image.substring(0, 1));
          break;
        default:
          jj_la1[21] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtn000.setImage( content.toString() );
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }


  final public String QuoteIndependentAttributeValueContent() throws ParseException {
        String tmp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EL_EXPRESSION_IN_ATTRIBUTE:
      tmp = ElExpressionInAttribute();
      break;
    case VALUE_BINDING_IN_ATTRIBUTE:
      tmp = ValueBindingInAttribute();
      break;
    case JSP_EXPRESSION_IN_ATTRIBUTE:
      tmp = JspExpressionInAttribute();
      break;
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
          {if (true) return tmp;}
    throw new RuntimeException("Missing return statement in function");
  }

  final public String JspExpressionInAttribute() throws ParseException {
 
        ASTJspExpressionInAttribute jjtn000 = new ASTJspExpressionInAttribute(this, JJTJSPEXPRESSIONINATTRIBUTE);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(JSP_EXPRESSION_IN_ATTRIBUTE);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(t.image.substring(3, t.image.length()-2).trim()); 
                {if (true) return t.image;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new RuntimeException("Missing return statement in function");
  }

  final public void CommentTag() throws ParseException {
 
        ASTCommentTag jjtn000 = new ASTCommentTag(this, JJTCOMMENTTAG);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);StringBuffer content = new StringBuffer();
        Token t;
    try {
      jj_consume_token(COMMENT_START);
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT_TEXT:
          ;
          break;
        default:
          jj_la1[24] = jj_gen;
          break label_10;
        }
        t = jj_consume_token(COMMENT_TEXT);
                         content.append(t.image);
      }
      jj_consume_token(COMMENT_END);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                jjtn000.setImage(content.toString().trim());
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void Declaration() throws ParseException {
 
        ASTDeclaration jjtn000 = new ASTDeclaration(this, JJTDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(DECL_START);
      t = jj_consume_token(TAG_NAME);
                   jjtn000.setName(t.image);
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ATTR_NAME:
          ;
          break;
        default:
          jj_la1[25] = jj_gen;
          break label_11;
        }
        Attribute();
      }
      jj_consume_token(DECL_END);
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void DoctypeDeclaration() throws ParseException {
 
        ASTDoctypeDeclaration jjtn000 = new ASTDoctypeDeclaration(this, JJTDOCTYPEDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(DOCTYPE_DECL_START);
      jj_consume_token(WHITESPACES);
      t = jj_consume_token(NAME);
                      jjtn000.setName(t.image);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WHITESPACES:
        jj_consume_token(WHITESPACES);
        break;
      default:
        jj_la1[26] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
      case SYSTEM:
        DoctypeExternalId();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case WHITESPACES:
          jj_consume_token(WHITESPACES);
          break;
        default:
          jj_la1[27] = jj_gen;
          ;
        }
        break;
      default:
        jj_la1[28] = jj_gen;
        ;
      }
      jj_consume_token(DOCTYPE_DECL_END);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void DoctypeExternalId() throws ParseException {
 
        ASTDoctypeExternalId jjtn000 = new ASTDoctypeExternalId(this, JJTDOCTYPEEXTERNALID);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token systemLiteral;
        Token pubIdLiteral;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SYSTEM:
        jj_consume_token(SYSTEM);
        jj_consume_token(WHITESPACES);
        systemLiteral = jj_consume_token(QUOTED_LITERAL);
                                  jjtree.closeNodeScope(jjtn000, true);
                                  jjtc000 = false;
                                  jjtn000.setUri(quoteContent(systemLiteral.image));
        break;
      case PUBLIC:
        jj_consume_token(PUBLIC);
        jj_consume_token(WHITESPACES);
        pubIdLiteral = jj_consume_token(QUOTED_LITERAL);
                                  jjtn000.setPublicId(quoteContent(pubIdLiteral.image));
        jj_consume_token(WHITESPACES);
        systemLiteral = jj_consume_token(QUOTED_LITERAL);
                                  jjtree.closeNodeScope(jjtn000, true);
                                  jjtc000 = false;
                                  jjtn000.setUri(quoteContent(systemLiteral.image));
        break;
      default:
        jj_la1[29] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
                  if (jjtc000) {
                    jjtree.closeNodeScope(jjtn000, true);
                  }
    }
  }

  final public void HtmlScript() throws ParseException {
 
        ASTHtmlScript jjtn000 = new ASTHtmlScript(this, JJTHTMLSCRIPT);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);StringBuffer content = new StringBuffer();
        Token t;
    try {
      label_12:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case HTML_SCRIPT_CONTENT:
          ;
          break;
        default:
          jj_la1[30] = jj_gen;
          break label_12;
        }
        t = jj_consume_token(HTML_SCRIPT_CONTENT);
                                      content.append(t.image);
      }
      jj_consume_token(HTML_SCRIPT_END_TAG);
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                        jjtn000.setImage(content.toString().trim());
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_3R_22() {
    if (jj_3R_26()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(48)) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_scan_token(DOCTYPE_DECL_START)) return true;
    if (jj_scan_token(WHITESPACES)) return true;
    if (jj_scan_token(NAME)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(48)) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_22()) jj_scanpos = xsp;
    if (jj_scan_token(DOCTYPE_DECL_END)) return true;
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_scan_token(ATTR_NAME)) return true;
    if (jj_scan_token(ATTR_EQ)) return true;
    if (jj_3R_28()) return true;
    return false;
  }

  private boolean jj_3R_41() {
    if (jj_scan_token(UNPARSED_TEXT_NO_DOUBLE_QUOTES)) return true;
    return false;
  }

  private boolean jj_3R_19() {
    if (jj_3R_25()) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_scan_token(DECL_START)) return true;
    if (jj_scan_token(TAG_NAME)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_19()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(DECL_END)) return true;
    return false;
  }

  private boolean jj_3R_21() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_43() {
    if (jj_scan_token(UNPARSED_TEXT_NO_SINGLE_QUOTES)) return true;
    return false;
  }

  private boolean jj_3R_27() {
    if (jj_scan_token(COMMENT_TEXT)) return true;
    return false;
  }

  private boolean jj_3R_23() {
    if (jj_scan_token(COMMENT_START)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_27()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(COMMENT_END)) return true;
    return false;
  }

  private boolean jj_3R_18() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_49() {
    if (jj_scan_token(JSP_EXPRESSION_IN_ATTRIBUTE)) return true;
    return false;
  }

  private boolean jj_3R_46() {
    if (jj_3R_49()) return true;
    return false;
  }

  private boolean jj_3R_45() {
    if (jj_3R_48()) return true;
    return false;
  }

  private boolean jj_3R_44() {
    if (jj_3R_47()) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_15() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) return true;
    }
    return false;
  }

  private boolean jj_3R_40() {
    if (jj_3R_42()) return true;
    return false;
  }

  private boolean jj_3_2() {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_15()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_36() {
    if (jj_scan_token(DOLLAR_OR_HASH_SINGLE_QUOTE)) return true;
    return false;
  }

  private boolean jj_3R_39() {
    if (jj_3R_43()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_17()) {
    jj_scanpos = xsp;
    if (jj_3R_18()) return true;
    }
    return false;
  }

  private boolean jj_3R_17() {
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_34() {
    if (jj_scan_token(DOLLAR_OR_HASH_DOUBLE_QUOTE)) return true;
    return false;
  }

  private boolean jj_3R_42() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_44()) {
    jj_scanpos = xsp;
    if (jj_3R_45()) {
    jj_scanpos = xsp;
    if (jj_3R_46()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_1() {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_13()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_35() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_39()) {
    jj_scanpos = xsp;
    if (jj_3R_40()) return true;
    }
    return false;
  }

  private boolean jj_3R_38() {
    if (jj_3R_42()) return true;
    return false;
  }

  private boolean jj_3R_37() {
    if (jj_3R_41()) return true;
    return false;
  }

  private boolean jj_3R_33() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_37()) {
    jj_scanpos = xsp;
    if (jj_3R_38()) return true;
    }
    return false;
  }

  private boolean jj_3R_32() {
    if (jj_scan_token(SINGLE_QUOTE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_35()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_scan_token(69)) {
    jj_scanpos = xsp;
    if (jj_3R_36()) return true;
    }
    return false;
  }

  private boolean jj_3R_24() {
    if (jj_scan_token(JSP_COMMENT_START)) return true;
    if (jj_scan_token(JSP_COMMENT_CONTENT)) return true;
    if (jj_scan_token(JSP_COMMENT_END)) return true;
    return false;
  }

  private boolean jj_3R_31() {
    if (jj_scan_token(DOUBLE_QUOTE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_33()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_scan_token(72)) {
    jj_scanpos = xsp;
    if (jj_3R_34()) return true;
    }
    return false;
  }

  private boolean jj_3R_30() {
    if (jj_scan_token(PUBLIC)) return true;
    if (jj_scan_token(WHITESPACES)) return true;
    if (jj_scan_token(QUOTED_LITERAL)) return true;
    if (jj_scan_token(WHITESPACES)) return true;
    if (jj_scan_token(QUOTED_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_47() {
    if (jj_scan_token(EL_EXPRESSION_IN_ATTRIBUTE)) return true;
    return false;
  }

  private boolean jj_3R_28() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_31()) {
    jj_scanpos = xsp;
    if (jj_3R_32()) return true;
    }
    return false;
  }

  private boolean jj_3R_26() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) return true;
    }
    return false;
  }

  private boolean jj_3R_29() {
    if (jj_scan_token(SYSTEM)) return true;
    if (jj_scan_token(WHITESPACES)) return true;
    if (jj_scan_token(QUOTED_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_48() {
    if (jj_scan_token(VALUE_BINDING_IN_ATTRIBUTE)) return true;
    return false;
  }

  
  public JspParserTokenManager token_source;
  
  public Token token;
  
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[31];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x11000000,0x11000000,0x11000000,0x11000000,0xf9400000,0xf9400000,0xf9400000,0x0,0x0,0x0,0x0,0x0,0x0,0xf9400000,0xf9400000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x7,0x1,0x1,0x6,0x10,0x6,0x6,0x400000,0x4000000,0x7,0x7,0x28000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4000000,0x10000,0x10000,0xc0000,0xc0000,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x6000,0x6000,0x0,0x21c,0x21c,0x500,0x5c,0x5c,0xa0,0x3,0x1c,0x1000,0x0,0x0,0x0,0x0,0x0,0x2000,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  
  public JspParser(CharStream stream) {
    token_source = new JspParserTokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  
  public void ReInit(CharStream stream) {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  
  public JspParser(JspParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  
  public void ReInit(JspParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }



  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }


  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[79];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 31; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 79; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  
  final public void enable_tracing() {
  }

  
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
