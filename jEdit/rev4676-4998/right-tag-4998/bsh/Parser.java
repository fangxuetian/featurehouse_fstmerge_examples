
package bsh;

import java.io.*;


public class Parser implements ParserTreeConstants, ParserConstants {
  protected JJTParserState jjtree = new JJTParserState();boolean retainComments = false;

        public void setRetainComments( boolean b ) {
                retainComments = b;
        }

        void jjtreeOpenNodeScope(Node n) {
                ((SimpleNode)n).firstToken = getToken(1);
        }

        void jjtreeCloseNodeScope(Node n) {
                ((SimpleNode)n).lastToken = getToken(0);
        }

        
        void reInitInput( Reader in ) {
                ReInit(in);
        }

        public SimpleNode popNode()
        {
                if ( jjtree.nodeArity() > 0)  
                        return (SimpleNode)jjtree.popNode();
                else
                        return null;
        }

        
        void reInitTokenInput( Reader in ) {
                jj_input_stream.ReInit( in,
                        jj_input_stream.getEndLine(),
                        jj_input_stream.getEndColumn() );
        }

        public static void main( String [] args )
                throws IOException, ParseException
        {
                boolean print = false;
                int i=0;
                if ( args[0].equals("-p") ) {
                        i++;
                        print=true;
                }
                for(; i< args.length; i++) {
                        Reader in = new FileReader(args[i]);
                        Parser parser = new Parser(in);
                        parser.setRetainComments(true);
                        while( !parser.Line() )
                                if ( print )
                                        System.out.println( parser.popNode() );
                }
        }

  final public boolean Line() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 0:
      jj_consume_token(0);
        Interpreter.debug("End of File!");
        {if (true) return true;}
      break;
    case BOOLEAN:
    case BREAK:
    case BYTE:
    case CHAR:
    case CONTINUE:
    case DO:
    case DOUBLE:
    case FALSE:
    case FINAL:
    case FLOAT:
    case FOR:
    case IF:
    case IMPORT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case RETURN:
    case SHORT:
    case SWITCH:
    case THROW:
    case TRUE:
    case TRY:
    case VOID:
    case WHILE:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case FORMAL_COMMENT:
    case IDENTIFIER:
    case LPAREN:
    case LBRACE:
    case SEMICOLON:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      if (jj_2_1(2147483647)) {
        Expression();
        jj_consume_token(SEMICOLON);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BREAK:
        case BYTE:
        case CHAR:
        case CONTINUE:
        case DO:
        case DOUBLE:
        case FALSE:
        case FINAL:
        case FLOAT:
        case FOR:
        case IF:
        case IMPORT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case RETURN:
        case SHORT:
        case SWITCH:
        case THROW:
        case TRUE:
        case TRY:
        case VOID:
        case WHILE:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case FORMAL_COMMENT:
        case IDENTIFIER:
        case LPAREN:
        case LBRACE:
        case SEMICOLON:
        case INCR:
        case DECR:
          BlockStatement();
          break;
        default:
          jj_la1[0] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
                {if (true) return false;}
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }


  final public void MethodDeclaration() throws ParseException {
 
  BSHMethodDeclaration jjtn000 = new BSHMethodDeclaration(JJTMETHODDECLARATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      if (jj_2_2(2147483647)) {
        ReturnType();
        t = jj_consume_token(IDENTIFIER);
                                    jjtn000.name = t.image;
        FormalParameters();
        Block();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
          t = jj_consume_token(IDENTIFIER);
                       jjtn000.name = t.image;
          FormalParameters();
          Block();
          break;
        default:
          jj_la1[2] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
        jjtreeCloseNodeScope(jjtn000);
      }
    }
  }

  final public void MethodDeclarationLookahead() throws ParseException {
    if (jj_2_3(2147483647)) {
      ReturnType();
      jj_consume_token(IDENTIFIER);
      FormalParameters();
      jj_consume_token(LBRACE);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        jj_consume_token(IDENTIFIER);
        FormalParameters();
        jj_consume_token(LBRACE);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void MethodDeclarationTypeLookahead() throws ParseException {
    ReturnType();
    jj_consume_token(IDENTIFIER);
    jj_consume_token(LPAREN);
  }

  final public void ImportDeclaration() throws ParseException {
 
    BSHImportDeclaration jjtn000 = new BSHImportDeclaration(JJTIMPORTDECLARATION);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      if (jj_2_4(2)) {
        jj_consume_token(IMPORT);
        AmbiguousName();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DOT:
          t = jj_consume_token(DOT);
          jj_consume_token(STAR);
          break;
        default:
          jj_la1[4] = jj_gen;
          ;
        }
        jj_consume_token(SEMICOLON);
                                                 jjtree.closeNodeScope(jjtn000, true);
                                                 jjtc000 = false;
                                                 jjtreeCloseNodeScope(jjtn000);
    if ( t != null )
        jjtn000.importPackage = true;
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IMPORT:
          jj_consume_token(IMPORT);
          jj_consume_token(STAR);
          jj_consume_token(SEMICOLON);
                     jjtree.closeNodeScope(jjtn000, true);
                     jjtc000 = false;
                     jjtreeCloseNodeScope(jjtn000);
                jjtn000.superImport = true;
          break;
        default:
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void VariableDeclarator() throws ParseException {
 
  BSHVariableDeclarator jjtn000 = new BSHVariableDeclarator(JJTVARIABLEDECLARATOR);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(IDENTIFIER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSIGN:
        jj_consume_token(ASSIGN);
        VariableInitializer();
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
                                                 jjtree.closeNodeScope(jjtn000, true);
                                                 jjtc000 = false;
                                                 jjtreeCloseNodeScope(jjtn000);
                                                 jjtn000.name = t.image;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void VariableInitializer() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      ArrayInitializer();
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      Expression();
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void ArrayInitializer() throws ParseException {
 
  BSHArrayInitializer jjtn000 = new BSHArrayInitializer(JJTARRAYINITIALIZER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(LBRACE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case LBRACE:
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        VariableInitializer();
        label_1:
        while (true) {
          if (jj_2_5(2)) {
            ;
          } else {
            break label_1;
          }
          jj_consume_token(COMMA);
          VariableInitializer();
        }
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        jj_consume_token(COMMA);
        break;
      default:
        jj_la1[9] = jj_gen;
        ;
      }
      jj_consume_token(RBRACE);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void FormalParameters() throws ParseException {
 
  BSHFormalParameters jjtn000 = new BSHFormalParameters(JJTFORMALPARAMETERS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(LPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
      case IDENTIFIER:
        FormalParameter();
        label_2:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COMMA:
            ;
            break;
          default:
            jj_la1[10] = jj_gen;
            break label_2;
          }
          jj_consume_token(COMMA);
          FormalParameter();
        }
        break;
      default:
        jj_la1[11] = jj_gen;
        ;
      }
      jj_consume_token(RPAREN);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void FormalParameter() throws ParseException {
 
  BSHFormalParameter jjtn000 = new BSHFormalParameter(JJTFORMALPARAMETER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);Token t;
    try {
      if (jj_2_6(2)) {
        Type();
        t = jj_consume_token(IDENTIFIER);
                                       jjtree.closeNodeScope(jjtn000, true);
                                       jjtc000 = false;
                                       jjtreeCloseNodeScope(jjtn000);
                                       jjtn000.name = t.image;
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
          t = jj_consume_token(IDENTIFIER);
                   jjtree.closeNodeScope(jjtn000, true);
                   jjtc000 = false;
                   jjtreeCloseNodeScope(jjtn000);
                   jjtn000.name = t.image;
          break;
        default:
          jj_la1[12] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void Type() throws ParseException {
 
  BSHType jjtn000 = new BSHType(JJTTYPE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
        PrimitiveType();
        break;
      case IDENTIFIER:
        AmbiguousName();
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_3:
      while (true) {
        if (jj_2_7(2)) {
          ;
        } else {
          break label_3;
        }
        jj_consume_token(LBRACKET);
        jj_consume_token(RBRACKET);
                                 jjtn000.addArrayDimension();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void ReturnType() throws ParseException {
 
  BSHReturnType jjtn000 = new BSHReturnType(JJTRETURNTYPE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VOID:
        jj_consume_token(VOID);
           jjtree.closeNodeScope(jjtn000, true);
           jjtc000 = false;
           jjtreeCloseNodeScope(jjtn000);
           jjtn000.isVoid = true;
        break;
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
      case IDENTIFIER:
        Type();
        break;
      default:
        jj_la1[14] = jj_gen;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void PrimitiveType() throws ParseException {
 
  BSHPrimitiveType jjtn000 = new BSHPrimitiveType(JJTPRIMITIVETYPE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
        jj_consume_token(BOOLEAN);
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.type = Boolean.TYPE;
        break;
      case CHAR:
        jj_consume_token(CHAR);
           jjtree.closeNodeScope(jjtn000, true);
           jjtc000 = false;
           jjtreeCloseNodeScope(jjtn000);
           jjtn000.type =  Character.TYPE;
        break;
      case BYTE:
        jj_consume_token(BYTE);
           jjtree.closeNodeScope(jjtn000, true);
           jjtc000 = false;
           jjtreeCloseNodeScope(jjtn000);
           jjtn000.type =  Byte.TYPE;
        break;
      case SHORT:
        jj_consume_token(SHORT);
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.type =  Short.TYPE;
        break;
      case INT:
        jj_consume_token(INT);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
          jjtn000.type =  Integer.TYPE;
        break;
      case LONG:
        jj_consume_token(LONG);
           jjtree.closeNodeScope(jjtn000, true);
           jjtc000 = false;
           jjtreeCloseNodeScope(jjtn000);
           jjtn000.type =  Long.TYPE;
        break;
      case FLOAT:
        jj_consume_token(FLOAT);
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.type =  Float.TYPE;
        break;
      case DOUBLE:
        jj_consume_token(DOUBLE);
             jjtree.closeNodeScope(jjtn000, true);
             jjtc000 = false;
             jjtreeCloseNodeScope(jjtn000);
             jjtn000.type =  Double.TYPE;
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
  if (jjtc000) {
    jjtree.closeNodeScope(jjtn000, true);
    jjtreeCloseNodeScope(jjtn000);
  }
    }
  }

  final public void AmbiguousName() throws ParseException {
 
    BSHAmbiguousName jjtn000 = new BSHAmbiguousName(JJTAMBIGUOUSNAME);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token t;
    StringBuffer s;
    try {
      t = jj_consume_token(IDENTIFIER);
        s = new StringBuffer(t.image);
      label_4:
      while (true) {
        if (jj_2_8(2)) {
          ;
        } else {
          break label_4;
        }
        jj_consume_token(DOT);
        t = jj_consume_token(IDENTIFIER);
        s.append("."+t.image);
      }
       jjtree.closeNodeScope(jjtn000, true);
       jjtc000 = false;
       jjtreeCloseNodeScope(jjtn000);
        jjtn000.text = s.toString();
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void Expression() throws ParseException {
    if (jj_2_9(2147483647)) {
      Assignment();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        ConditionalExpression();
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void Assignment() throws ParseException {
 
  BSHAssignment jjtn000 = new BSHAssignment(JJTASSIGNMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);int op ;
    try {
      LHSPrimaryExpression();
      op = AssignmentOperator();
    jjtn000.operator = op;
      Expression();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public int AssignmentOperator() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ASSIGN:
      jj_consume_token(ASSIGN);
      break;
    case STARASSIGN:
      jj_consume_token(STARASSIGN);
      break;
    case SLASHASSIGN:
      jj_consume_token(SLASHASSIGN);
      break;
    case MODASSIGN:
      jj_consume_token(MODASSIGN);
      break;
    case PLUSASSIGN:
      jj_consume_token(PLUSASSIGN);
      break;
    case MINUSASSIGN:
      jj_consume_token(MINUSASSIGN);
      break;
    case ANDASSIGN:
      jj_consume_token(ANDASSIGN);
      break;
    case XORASSIGN:
      jj_consume_token(XORASSIGN);
      break;
    case ORASSIGN:
      jj_consume_token(ORASSIGN);
      break;
    case LSHIFTASSIGN:
      jj_consume_token(LSHIFTASSIGN);
      break;
    case LSHIFTASSIGNX:
      jj_consume_token(LSHIFTASSIGNX);
      break;
    case RSIGNEDSHIFTASSIGN:
      jj_consume_token(RSIGNEDSHIFTASSIGN);
      break;
    case RSIGNEDSHIFTASSIGNX:
      jj_consume_token(RSIGNEDSHIFTASSIGNX);
      break;
    case RUNSIGNEDSHIFTASSIGN:
      jj_consume_token(RUNSIGNEDSHIFTASSIGN);
      break;
    case RUNSIGNEDSHIFTASSIGNX:
      jj_consume_token(RUNSIGNEDSHIFTASSIGNX);
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        t = getToken(0);
        {if (true) return t.kind;}
    throw new Error("Missing return statement in function");
  }

  final public void ConditionalExpression() throws ParseException {
    ConditionalOrExpression();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HOOK:
      jj_consume_token(HOOK);
      Expression();
      jj_consume_token(COLON);
                                                     BSHTernaryExpression jjtn001 = new BSHTernaryExpression(JJTTERNARYEXPRESSION);
                                                     boolean jjtc001 = true;
                                                     jjtree.openNodeScope(jjtn001);
                                                     jjtreeOpenNodeScope(jjtn001);
      try {
        ConditionalExpression();
      } catch (Throwable jjte001) {
                                                     if (jjtc001) {
                                                       jjtree.clearNodeScope(jjtn001);
                                                       jjtc001 = false;
                                                     } else {
                                                       jjtree.popNode();
                                                     }
                                                     if (jjte001 instanceof RuntimeException) {
                                                       {if (true) throw (RuntimeException)jjte001;}
                                                     }
                                                     if (jjte001 instanceof ParseException) {
                                                       {if (true) throw (ParseException)jjte001;}
                                                     }
                                                     {if (true) throw (Error)jjte001;}
      } finally {
                                                     if (jjtc001) {
                                                       jjtree.closeNodeScope(jjtn001,  3);
                                                       jjtreeCloseNodeScope(jjtn001);
                                                     }
      }
      break;
    default:
      jj_la1[18] = jj_gen;
      ;
    }
  }

  final public void ConditionalOrExpression() throws ParseException {
  Token t=null;
    ConditionalAndExpression();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_OR:
      case BOOL_ORX:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_OR:
        t = jj_consume_token(BOOL_OR);
        break;
      case BOOL_ORX:
        t = jj_consume_token(BOOL_ORX);
        break;
      default:
        jj_la1[20] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ConditionalAndExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void ConditionalAndExpression() throws ParseException {
  Token t=null;
    InclusiveOrExpression();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_AND:
      case BOOL_ANDX:
        ;
        break;
      default:
        jj_la1[21] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_AND:
        t = jj_consume_token(BOOL_AND);
        break;
      case BOOL_ANDX:
        t = jj_consume_token(BOOL_ANDX);
        break;
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      InclusiveOrExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void InclusiveOrExpression() throws ParseException {
  Token t=null;
    ExclusiveOrExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_OR:
      case BIT_ORX:
        ;
        break;
      default:
        jj_la1[23] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_OR:
        t = jj_consume_token(BIT_OR);
        break;
      case BIT_ORX:
        t = jj_consume_token(BIT_ORX);
        break;
      default:
        jj_la1[24] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ExclusiveOrExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void ExclusiveOrExpression() throws ParseException {
  Token t=null;
    AndExpression();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case XOR:
        ;
        break;
      default:
        jj_la1[25] = jj_gen;
        break label_8;
      }
      t = jj_consume_token(XOR);
      AndExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void AndExpression() throws ParseException {
  Token t=null;
    EqualityExpression();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
      case BIT_ANDX:
        ;
        break;
      default:
        jj_la1[26] = jj_gen;
        break label_9;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
        t = jj_consume_token(BIT_AND);
        break;
      case BIT_ANDX:
        t = jj_consume_token(BIT_ANDX);
        break;
      default:
        jj_la1[27] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      EqualityExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void EqualityExpression() throws ParseException {
  Token t = null;
    InstanceOfExpression();
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
      case NE:
        ;
        break;
      default:
        jj_la1[28] = jj_gen;
        break label_10;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
        t = jj_consume_token(EQ);
        break;
      case NE:
        t = jj_consume_token(NE);
        break;
      default:
        jj_la1[29] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      InstanceOfExpression();
      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
    }
  }

  final public void InstanceOfExpression() throws ParseException {
  Token t = null;
    RelationalExpression();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INSTANCEOF:
      t = jj_consume_token(INSTANCEOF);
      Type();
                              BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
                              boolean jjtc001 = true;
                              jjtree.openNodeScope(jjtn001);
                              jjtreeOpenNodeScope(jjtn001);
      try {
                              jjtree.closeNodeScope(jjtn001,  2);
                              jjtc001 = false;
                              jjtreeCloseNodeScope(jjtn001);
                              jjtn001.kind = t.kind;
      } finally {
                              if (jjtc001) {
                                jjtree.closeNodeScope(jjtn001,  2);
                                jjtreeCloseNodeScope(jjtn001);
                              }
      }
      break;
    default:
      jj_la1[30] = jj_gen;
      ;
    }
  }

  final public void RelationalExpression() throws ParseException {
  Token t = null;
    ShiftExpression();
    label_11:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GT:
      case GTX:
      case LT:
      case LTX:
      case LE:
      case LEX:
      case GE:
      case GEX:
        ;
        break;
      default:
        jj_la1[31] = jj_gen;
        break label_11;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LT:
        t = jj_consume_token(LT);
        break;
      case LTX:
        t = jj_consume_token(LTX);
        break;
      case GT:
        t = jj_consume_token(GT);
        break;
      case GTX:
        t = jj_consume_token(GTX);
        break;
      case LE:
        t = jj_consume_token(LE);
        break;
      case LEX:
        t = jj_consume_token(LEX);
        break;
      case GE:
        t = jj_consume_token(GE);
        break;
      case GEX:
        t = jj_consume_token(GEX);
        break;
      default:
        jj_la1[32] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ShiftExpression();
    BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    jjtreeOpenNodeScope(jjtn001);
      try {
    jjtree.closeNodeScope(jjtn001,  2);
    jjtc001 = false;
    jjtreeCloseNodeScope(jjtn001);
    jjtn001.kind = t.kind;
      } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtreeCloseNodeScope(jjtn001);
    }
      }
    }
  }

  final public void ShiftExpression() throws ParseException {
  Token t = null;
    AdditiveExpression();
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LSHIFT:
      case LSHIFTX:
      case RSIGNEDSHIFT:
      case RSIGNEDSHIFTX:
      case RUNSIGNEDSHIFT:
      case RUNSIGNEDSHIFTX:
        ;
        break;
      default:
        jj_la1[33] = jj_gen;
        break label_12;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LSHIFT:
        t = jj_consume_token(LSHIFT);
        break;
      case LSHIFTX:
        t = jj_consume_token(LSHIFTX);
        break;
      case RSIGNEDSHIFT:
        t = jj_consume_token(RSIGNEDSHIFT);
        break;
      case RSIGNEDSHIFTX:
        t = jj_consume_token(RSIGNEDSHIFTX);
        break;
      case RUNSIGNEDSHIFT:
        t = jj_consume_token(RUNSIGNEDSHIFT);
        break;
      case RUNSIGNEDSHIFTX:
        t = jj_consume_token(RUNSIGNEDSHIFTX);
        break;
      default:
        jj_la1[34] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      AdditiveExpression();
    BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    jjtreeOpenNodeScope(jjtn001);
      try {
    jjtree.closeNodeScope(jjtn001,  2);
    jjtc001 = false;
    jjtreeCloseNodeScope(jjtn001);
    jjtn001.kind = t.kind;
      } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtreeCloseNodeScope(jjtn001);
    }
      }
    }
  }

  final public void AdditiveExpression() throws ParseException {
  Token t = null;
    MultiplicativeExpression();
    label_13:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[35] = jj_gen;
        break label_13;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        t = jj_consume_token(PLUS);
        break;
      case MINUS:
        t = jj_consume_token(MINUS);
        break;
      default:
        jj_la1[36] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      MultiplicativeExpression();
                                                     BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
                                                     boolean jjtc001 = true;
                                                     jjtree.openNodeScope(jjtn001);
                                                     jjtreeOpenNodeScope(jjtn001);
      try {
                                                     jjtree.closeNodeScope(jjtn001,  2);
                                                     jjtc001 = false;
                                                     jjtreeCloseNodeScope(jjtn001);
                                                     jjtn001.kind = t.kind;
      } finally {
                                                     if (jjtc001) {
                                                       jjtree.closeNodeScope(jjtn001,  2);
                                                       jjtreeCloseNodeScope(jjtn001);
                                                     }
      }
    }
  }

  final public void MultiplicativeExpression() throws ParseException {
  Token t = null;
    UnaryExpression();
    label_14:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
      case SLASH:
      case MOD:
        ;
        break;
      default:
        jj_la1[37] = jj_gen;
        break label_14;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
        t = jj_consume_token(STAR);
        break;
      case SLASH:
        t = jj_consume_token(SLASH);
        break;
      case MOD:
        t = jj_consume_token(MOD);
        break;
      default:
        jj_la1[38] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      UnaryExpression();
                      BSHBinaryExpression jjtn001 = new BSHBinaryExpression(JJTBINARYEXPRESSION);
                      boolean jjtc001 = true;
                      jjtree.openNodeScope(jjtn001);
                      jjtreeOpenNodeScope(jjtn001);
      try {
                      jjtree.closeNodeScope(jjtn001,  2);
                      jjtc001 = false;
                      jjtreeCloseNodeScope(jjtn001);
                      jjtn001.kind = t.kind;
      } finally {
                      if (jjtc001) {
                        jjtree.closeNodeScope(jjtn001,  2);
                        jjtreeCloseNodeScope(jjtn001);
                      }
      }
    }
  }

  final public void UnaryExpression() throws ParseException {
  Token t = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
    case MINUS:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        t = jj_consume_token(PLUS);
        break;
      case MINUS:
        t = jj_consume_token(MINUS);
        break;
      default:
        jj_la1[39] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      UnaryExpression();
      BSHUnaryExpression jjtn001 = new BSHUnaryExpression(JJTUNARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  1);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  1);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
      break;
    case INCR:
      PreIncrementExpression();
      break;
    case DECR:
      PreDecrementExpression();
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
      UnaryExpressionNotPlusMinus();
      break;
    default:
      jj_la1[40] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void PreIncrementExpression() throws ParseException {
  Token t = null;
    t = jj_consume_token(INCR);
    LHSPrimaryExpression();
      BSHUnaryExpression jjtn001 = new BSHUnaryExpression(JJTUNARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
    try {
      jjtree.closeNodeScope(jjtn001,  1);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
    } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  1);
        jjtreeCloseNodeScope(jjtn001);
      }
    }
  }

  final public void PreDecrementExpression() throws ParseException {
  Token t = null;
    t = jj_consume_token(DECR);
    LHSPrimaryExpression();
      BSHUnaryExpression jjtn001 = new BSHUnaryExpression(JJTUNARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
    try {
      jjtree.closeNodeScope(jjtn001,  1);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
    } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  1);
        jjtreeCloseNodeScope(jjtn001);
      }
    }
  }

  final public void UnaryExpressionNotPlusMinus() throws ParseException {
  Token t = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BANG:
    case TILDE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TILDE:
        t = jj_consume_token(TILDE);
        break;
      case BANG:
        t = jj_consume_token(BANG);
        break;
      default:
        jj_la1[41] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      UnaryExpression();
      BSHUnaryExpression jjtn001 = new BSHUnaryExpression(JJTUNARYEXPRESSION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      jjtreeOpenNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  1);
      jjtc001 = false;
      jjtreeCloseNodeScope(jjtn001);
      jjtn001.kind = t.kind;
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  1);
        jjtreeCloseNodeScope(jjtn001);
      }
      }
      break;
    default:
      jj_la1[42] = jj_gen;
      if (jj_2_10(2147483647)) {
        CastExpression();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
          PostfixExpression();
          break;
        default:
          jj_la1[43] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }


  final public void CastLookahead() throws ParseException {
    if (jj_2_11(2)) {
      jj_consume_token(LPAREN);
      PrimitiveType();
    } else if (jj_2_12(2147483647)) {
      jj_consume_token(LPAREN);
      AmbiguousName();
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        AmbiguousName();
        jj_consume_token(RPAREN);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TILDE:
          jj_consume_token(TILDE);
          break;
        case BANG:
          jj_consume_token(BANG);
          break;
        case LPAREN:
          jj_consume_token(LPAREN);
          break;
        case IDENTIFIER:
          jj_consume_token(IDENTIFIER);
          break;
        case NEW:
          jj_consume_token(NEW);
          break;
        case FALSE:
        case NULL:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
          Literal();
          break;
        default:
          jj_la1[44] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[45] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void PostfixExpression() throws ParseException {
  Token t = null;
    if (jj_2_13(2147483647)) {
      LHSPrimaryExpression();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INCR:
        t = jj_consume_token(INCR);
        break;
      case DECR:
        t = jj_consume_token(DECR);
        break;
      default:
        jj_la1[46] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                                BSHUnaryExpression jjtn001 = new BSHUnaryExpression(JJTUNARYEXPRESSION);
                                boolean jjtc001 = true;
                                jjtree.openNodeScope(jjtn001);
                                jjtreeOpenNodeScope(jjtn001);
      try {
                                jjtree.closeNodeScope(jjtn001,  1);
                                jjtc001 = false;
                                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind; jjtn001.postfix = true;
      } finally {
                                if (jjtc001) {
                                  jjtree.closeNodeScope(jjtn001,  1);
                                  jjtreeCloseNodeScope(jjtn001);
                                }
      }
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
        PrimaryExpression();
        break;
      default:
        jj_la1[47] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void CastExpression() throws ParseException {
 
  BSHCastExpression jjtn000 = new BSHCastExpression(JJTCASTEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      if (jj_2_14(2147483647)) {
        jj_consume_token(LPAREN);
        Type();
        jj_consume_token(RPAREN);
        UnaryExpression();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPAREN:
          jj_consume_token(LPAREN);
          Type();
          jj_consume_token(RPAREN);
          UnaryExpressionNotPlusMinus();
          break;
        default:
          jj_la1[48] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void PrimaryExpression() throws ParseException {
                                               
  BSHPrimaryExpression jjtn000 = new BSHPrimaryExpression(JJTPRIMARYEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      PrimaryPrefix();
      label_15:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case LBRACKET:
        case DOT:
          ;
          break;
        default:
          jj_la1[49] = jj_gen;
          break label_15;
        }
        PrimarySuffix();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void MethodInvocation() throws ParseException {
                                             
  BSHMethodInvocation jjtn000 = new BSHMethodInvocation(JJTMETHODINVOCATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      AmbiguousName();
      Arguments();
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
       jjtreeCloseNodeScope(jjtn000);
     }
    }
  }

  final public void PrimaryPrefix() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FALSE:
    case NULL:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
      Literal();
      break;
    case LPAREN:
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      break;
    case NEW:
      AllocationExpression();
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FLOAT:
    case INT:
    case LONG:
    case SHORT:
    case IDENTIFIER:
      if (jj_2_16(2147483647)) {
        MethodInvocation();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case IDENTIFIER:
          if (jj_2_15(2147483647)) {
            Type();
          } else {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case IDENTIFIER:
              AmbiguousName();
              break;
            default:
              jj_la1[50] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
          }
          break;
        default:
          jj_la1[51] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    default:
      jj_la1[52] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void PrimarySuffix() throws ParseException {
 
    BSHPrimarySuffix jjtn000 = new BSHPrimarySuffix(JJTPRIMARYSUFFIX);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      if (jj_2_17(2)) {
        jj_consume_token(DOT);
        jj_consume_token(CLASS);
                jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
                jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHPrimarySuffix.CLASS;
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACKET:
          jj_consume_token(LBRACKET);
          Expression();
          jj_consume_token(RBRACKET);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHPrimarySuffix.INDEX;
          break;
        case DOT:
          jj_consume_token(DOT);
          t = jj_consume_token(IDENTIFIER);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LPAREN:
            Arguments();
            break;
          default:
            jj_la1[53] = jj_gen;
            ;
          }
                                         jjtree.closeNodeScope(jjtn000, true);
                                         jjtc000 = false;
                                         jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHPrimarySuffix.NAME;
        jjtn000.field = t.image;
          break;
        case LBRACE:
          jj_consume_token(LBRACE);
          Expression();
          jj_consume_token(RBRACE);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHPrimarySuffix.PROPERTY;
          break;
        default:
          jj_la1[54] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }



  final public void LHSPrimaryPrefix() throws ParseException {
    if (jj_2_18(2147483647)) {
      MethodInvocation();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        AmbiguousName();
        break;
      default:
        jj_la1[55] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void LHSPrimaryExpression() throws ParseException {
                                                     
  BSHLHSPrimaryExpression jjtn000 = new BSHLHSPrimaryExpression(JJTLHSPRIMARYEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      LHSPrimaryPrefix();
      label_16:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case LBRACKET:
        case DOT:
          ;
          break;
        default:
          jj_la1[56] = jj_gen;
          break label_16;
        }
        LHSPrimarySuffix();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void LHSPrimarySuffix() throws ParseException {
 
    BSHLHSPrimarySuffix jjtn000 = new BSHLHSPrimarySuffix(JJTLHSPRIMARYSUFFIX);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token t=null, t1, t2 = null;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        jj_consume_token(LBRACKET);
        Expression();
        jj_consume_token(RBRACKET);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHLHSPrimarySuffix.INDEX;
        break;
      case DOT:
        jj_consume_token(DOT);
        t1 = jj_consume_token(IDENTIFIER);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPAREN:
          Arguments();
          jj_consume_token(DOT);
          t2 = jj_consume_token(IDENTIFIER);
          break;
        default:
          jj_la1[57] = jj_gen;
          ;
        }
                                                                jjtree.closeNodeScope(jjtn000, true);
                                                                jjtc000 = false;
                                                                jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHLHSPrimarySuffix.NAME;
        if ( t2 == null )
            jjtn000.field = t1.image;
        else {
            jjtn000.method = t1.image;
            jjtn000.field = t2.image;
        }
        break;
      case LBRACE:
        jj_consume_token(LBRACE);
        Expression();
        jj_consume_token(RBRACE);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtreeCloseNodeScope(jjtn000);
        jjtn000.operation = BSHLHSPrimarySuffix.PROPERTY;
        break;
      default:
        jj_la1[58] = jj_gen;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void Literal() throws ParseException {
 
    BSHLiteral jjtn000 = new BSHLiteral(JJTLITERAL);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token x;
    boolean b;
    String literal;
    char ch;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
        x = jj_consume_token(INTEGER_LITERAL);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtreeCloseNodeScope(jjtn000);
    literal = x.image;
    ch = literal.charAt(literal.length()-1);
    if(ch == 'l' || ch == 'L')
    {
        literal = literal.substring(0,literal.length()-1);

        
        
        jjtn000.value = new Primitive( new Long( literal ) );
    }
    else
        jjtn000.value = new Primitive( Integer.decode( literal ) );
        break;
      case FLOATING_POINT_LITERAL:
        x = jj_consume_token(FLOATING_POINT_LITERAL);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtreeCloseNodeScope(jjtn000);
    literal = x.image;
    ch = literal.charAt(literal.length()-1);
    if(ch == 'f' || ch == 'F')
    {
        literal = literal.substring(0,literal.length()-1);
        jjtn000.value = new Primitive( new Float( literal ) );
    }
    else
    {
        if(ch == 'd' || ch == 'D')
            literal = literal.substring(0,literal.length()-1);

        jjtn000.value = new Primitive( new Double( literal ) );
    }
        break;
      case CHARACTER_LITERAL:
        x = jj_consume_token(CHARACTER_LITERAL);
                            jjtree.closeNodeScope(jjtn000, true);
                            jjtc000 = false;
                            jjtreeCloseNodeScope(jjtn000);
                try {
                jjtn000.charSetup( x.image.substring(1, x.image.length() - 1) );
                } catch ( Exception e ) {
                        {if (true) throw new ParseException("Error parsing character: "+x.image);}
                }
        break;
      case STRING_LITERAL:
        x = jj_consume_token(STRING_LITERAL);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtreeCloseNodeScope(jjtn000);
                try {
                        jjtn000.stringSetup( x.image.substring(1, x.image.length() - 1) );
                } catch ( Exception e ) {
                        {if (true) throw new ParseException("Error parsing string: "+x.image);}
                }
        break;
      case FALSE:
      case TRUE:
        b = BooleanLiteral();
                          jjtree.closeNodeScope(jjtn000, true);
                          jjtc000 = false;
                          jjtreeCloseNodeScope(jjtn000);
    jjtn000.value = new Primitive( new Boolean(b) );
        break;
      case NULL:
        NullLiteral();
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                  jjtreeCloseNodeScope(jjtn000);
    jjtn000.value = Primitive.NULL;
        break;
      case VOID:
        VoidLiteral();
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                 jjtreeCloseNodeScope(jjtn000);
    jjtn000.value = Primitive.VOID;
        break;
      default:
        jj_la1[59] = jj_gen;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public boolean BooleanLiteral() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
           {if (true) return true;}
      break;
    case FALSE:
      jj_consume_token(FALSE);
            {if (true) return false;}
      break;
    default:
      jj_la1[60] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void NullLiteral() throws ParseException {
    jj_consume_token(NULL);
  }

  final public void VoidLiteral() throws ParseException {
    jj_consume_token(VOID);
  }

  final public void Arguments() throws ParseException {
 
  BSHArguments jjtn000 = new BSHArguments(JJTARGUMENTS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(LPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        ArgumentList();
        break;
      default:
        jj_la1[61] = jj_gen;
        ;
      }
      jj_consume_token(RPAREN);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void ArgumentList() throws ParseException {
    Expression();
    label_17:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[62] = jj_gen;
        break label_17;
      }
      jj_consume_token(COMMA);
      Expression();
    }
  }

  final public void AllocationExpression() throws ParseException {
 
  BSHAllocationExpression jjtn000 = new BSHAllocationExpression(JJTALLOCATIONEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      if (jj_2_20(2)) {
        jj_consume_token(NEW);
        PrimitiveType();
        ArrayDimensions();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NEW:
          jj_consume_token(NEW);
          AmbiguousName();
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LBRACKET:
            ArrayDimensions();
            break;
          case LPAREN:
            Arguments();
            if (jj_2_19(2)) {
              Block();
            } else {
              ;
            }
            break;
          default:
            jj_la1[63] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[64] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ArrayDimensions() throws ParseException {
 
  BSHArrayDimensions jjtn000 = new BSHArrayDimensions(JJTARRAYDIMENSIONS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      if (jj_2_22(2)) {
        label_18:
        while (true) {
          jj_consume_token(LBRACKET);
          Expression();
          jj_consume_token(RBRACKET);
                                        jjtn000.addArrayDimension();
          if (jj_2_21(2)) {
            ;
          } else {
            break label_18;
          }
        }
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACKET:
          label_19:
          while (true) {
            jj_consume_token(LBRACKET);
            jj_consume_token(RBRACKET);
              jjtn000.addArrayDimension();
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case LBRACKET:
              ;
              break;
            default:
              jj_la1[65] = jj_gen;
              break label_19;
            }
          }
          ArrayInitializer();
          break;
        default:
          jj_la1[66] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void Statement() throws ParseException {
    if (jj_2_23(2)) {
      LabeledStatement();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        Block();
        break;
      case SEMICOLON:
        EmptyStatement();
        break;
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        StatementExpression();
        jj_consume_token(SEMICOLON);
        break;
      case SWITCH:
        SwitchStatement();
        break;
      case IF:
        IfStatement();
        break;
      case WHILE:
        WhileStatement();
        break;
      case DO:
        DoStatement();
        break;
      case FOR:
        ForStatement();
        break;
      case BREAK:
        BreakStatement();
        break;
      case CONTINUE:
        ContinueStatement();
        break;
      case RETURN:
        ReturnStatement();
        break;
      case THROW:
        ThrowStatement();
        break;
      case TRY:
        TryStatement();
        break;
      default:
        jj_la1[67] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void LabeledStatement() throws ParseException {
    jj_consume_token(IDENTIFIER);
    jj_consume_token(COLON);
    Statement();
  }

  final public void Block() throws ParseException {
 
  BSHBlock jjtn000 = new BSHBlock(JJTBLOCK);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(LBRACE);
      label_20:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BREAK:
        case BYTE:
        case CHAR:
        case CONTINUE:
        case DO:
        case DOUBLE:
        case FALSE:
        case FINAL:
        case FLOAT:
        case FOR:
        case IF:
        case IMPORT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case RETURN:
        case SHORT:
        case SWITCH:
        case THROW:
        case TRUE:
        case TRY:
        case VOID:
        case WHILE:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case FORMAL_COMMENT:
        case IDENTIFIER:
        case LPAREN:
        case LBRACE:
        case SEMICOLON:
        case INCR:
        case DECR:
          ;
          break;
        default:
          jj_la1[68] = jj_gen;
          break label_20;
        }
        BlockStatement();
      }
      jj_consume_token(RBRACE);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void BlockStatement() throws ParseException {
    if (jj_2_24(2147483647)) {
      MethodDeclaration();
    } else if (jj_2_25(2147483647)) {
      TypedVariableDeclaration();
      jj_consume_token(SEMICOLON);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BREAK:
      case BYTE:
      case CHAR:
      case CONTINUE:
      case DO:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case FOR:
      case IF:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case RETURN:
      case SHORT:
      case SWITCH:
      case THROW:
      case TRUE:
      case TRY:
      case VOID:
      case WHILE:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case LBRACE:
      case SEMICOLON:
      case INCR:
      case DECR:
        Statement();
        break;
      case IMPORT:
        ImportDeclaration();
        break;
      case FORMAL_COMMENT:
        FormalComment();
        break;
      default:
        jj_la1[69] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void FormalComment() throws ParseException {
 
        BSHFormalComment jjtn000 = new BSHFormalComment(JJTFORMALCOMMENT);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(FORMAL_COMMENT);
                              jjtree.closeNodeScope(jjtn000,  retainComments);
                              jjtc000 = false;
                              jjtreeCloseNodeScope(jjtn000);
                jjtn000.text=t.image;
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000,  retainComments);
            jjtreeCloseNodeScope(jjtn000);
          }
    }
  }

  final public void EmptyStatement() throws ParseException {
    jj_consume_token(SEMICOLON);
  }

  final public void StatementExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INCR:
      PreIncrementExpression();
      break;
    case DECR:
      PreDecrementExpression();
      break;
    default:
      jj_la1[70] = jj_gen;
      if (jj_2_26(2147483647)) {
        Assignment();

      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
          PostfixExpression();
          break;
        default:
          jj_la1[71] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

  final public void SwitchStatement() throws ParseException {
 
  BSHSwitchStatement jjtn000 = new BSHSwitchStatement(JJTSWITCHSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(SWITCH);
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      jj_consume_token(LBRACE);
      label_21:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CASE:
        case _DEFAULT:
          ;
          break;
        default:
          jj_la1[72] = jj_gen;
          break label_21;
        }
        SwitchLabel();
        label_22:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case BOOLEAN:
          case BREAK:
          case BYTE:
          case CHAR:
          case CONTINUE:
          case DO:
          case DOUBLE:
          case FALSE:
          case FINAL:
          case FLOAT:
          case FOR:
          case IF:
          case IMPORT:
          case INT:
          case LONG:
          case NEW:
          case NULL:
          case RETURN:
          case SHORT:
          case SWITCH:
          case THROW:
          case TRUE:
          case TRY:
          case VOID:
          case WHILE:
          case INTEGER_LITERAL:
          case FLOATING_POINT_LITERAL:
          case CHARACTER_LITERAL:
          case STRING_LITERAL:
          case FORMAL_COMMENT:
          case IDENTIFIER:
          case LPAREN:
          case LBRACE:
          case SEMICOLON:
          case INCR:
          case DECR:
            ;
            break;
          default:
            jj_la1[73] = jj_gen;
            break label_22;
          }
          BlockStatement();
        }
      }
      jj_consume_token(RBRACE);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void SwitchLabel() throws ParseException {
 
  BSHSwitchLabel jjtn000 = new BSHSwitchLabel(JJTSWITCHLABEL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CASE:
        jj_consume_token(CASE);
        Expression();
        jj_consume_token(COLON);
        break;
      case _DEFAULT:
        jj_consume_token(_DEFAULT);
        jj_consume_token(COLON);
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                  jjtreeCloseNodeScope(jjtn000);
                  jjtn000.isDefault = true;
        break;
      default:
        jj_la1[74] = jj_gen;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void IfStatement() throws ParseException {
 
  BSHIfStatement jjtn000 = new BSHIfStatement(JJTIFSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(IF);
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      Statement();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ELSE:
        jj_consume_token(ELSE);
        Statement();
        break;
      default:
        jj_la1[75] = jj_gen;
        ;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void WhileStatement() throws ParseException {
 
  BSHWhileStatement jjtn000 = new BSHWhileStatement(JJTWHILESTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(WHILE);
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      Statement();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }


  final public void DoStatement() throws ParseException {
 
  BSHWhileStatement jjtn000 = new BSHWhileStatement(JJTWHILESTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(DO);
      Statement();
      jj_consume_token(WHILE);
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      jj_consume_token(SEMICOLON);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
          jjtn000.isDoStatement=true;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ForStatement() throws ParseException {
 
  BSHForStatement jjtn000 = new BSHForStatement(JJTFORSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(FOR);
      jj_consume_token(LPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FINAL:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        ForInit();
                          jjtn000.hasForInit=true;
        break;
      default:
        jj_la1[76] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        Expression();
                         jjtn000.hasExpression=true;
        break;
      default:
        jj_la1[77] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        ForUpdate();
                        jjtn000.hasForUpdate=true;
        break;
      default:
        jj_la1[78] = jj_gen;
        ;
      }
      jj_consume_token(RPAREN);
      Statement();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ForInit() throws ParseException {
  Token t = null;
    if (jj_2_27(2147483647)) {
      TypedVariableDeclaration();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        StatementExpressionList();
        break;
      default:
        jj_la1[79] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }


  final public void TypedVariableDeclaration() throws ParseException {
 
        BSHTypedVariableDeclaration jjtn000 = new BSHTypedVariableDeclaration(JJTTYPEDVARIABLEDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FINAL:
        t = jj_consume_token(FINAL);
        break;
      default:
        jj_la1[80] = jj_gen;
        ;
      }
      Type();
      VariableDeclarator();
      label_23:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[81] = jj_gen;
          break label_23;
        }
        jj_consume_token(COMMA);
        VariableDeclarator();
      }
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
        jjtn000.isFinal = (t!=null);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void StatementExpressionList() throws ParseException {
 
  BSHStatementExpressionList jjtn000 = new BSHStatementExpressionList(JJTSTATEMENTEXPRESSIONLIST);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      StatementExpression();
      label_24:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[82] = jj_gen;
          break label_24;
        }
        jj_consume_token(COMMA);
        StatementExpression();
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ForUpdate() throws ParseException {
    StatementExpressionList();
  }

  final public void BreakStatement() throws ParseException {
 
  BSHReturnStatement jjtn000 = new BSHReturnStatement(JJTRETURNSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(BREAK);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        jj_consume_token(IDENTIFIER);
        break;
      default:
        jj_la1[83] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
                                 jjtree.closeNodeScope(jjtn000, true);
                                 jjtc000 = false;
                                 jjtreeCloseNodeScope(jjtn000);
                                 jjtn000.kind = BREAK;
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ContinueStatement() throws ParseException {
 
  BSHReturnStatement jjtn000 = new BSHReturnStatement(JJTRETURNSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(CONTINUE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        jj_consume_token(IDENTIFIER);
        break;
      default:
        jj_la1[84] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
                                    jjtree.closeNodeScope(jjtn000, true);
                                    jjtc000 = false;
                                    jjtreeCloseNodeScope(jjtn000);
                                    jjtn000.kind = CONTINUE;
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ReturnStatement() throws ParseException {
 
  BSHReturnStatement jjtn000 = new BSHReturnStatement(JJTRETURNSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(RETURN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        Expression();
        break;
      default:
        jj_la1[85] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
                                  jjtree.closeNodeScope(jjtn000, true);
                                  jjtc000 = false;
                                  jjtreeCloseNodeScope(jjtn000);
                                  jjtn000.kind = RETURN;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void ThrowStatement() throws ParseException {
 
  BSHThrowStatement jjtn000 = new BSHThrowStatement(JJTTHROWSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(THROW);
      Expression();
      jj_consume_token(SEMICOLON);
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final public void TryStatement() throws ParseException {
 
  BSHTryStatement jjtn000 = new BSHTryStatement(JJTTRYSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(TRY);
      Block();
      label_25:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CATCH:
          ;
          break;
        default:
          jj_la1[86] = jj_gen;
          break label_25;
        }
        jj_consume_token(CATCH);
        jj_consume_token(LPAREN);
        FormalParameter();
        jj_consume_token(RPAREN);
        Block();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FINALLY:
        jj_consume_token(FINALLY);
        Block();
        break;
      default:
        jj_la1[87] = jj_gen;
        ;
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
      jjtreeCloseNodeScope(jjtn000);
    }
    }
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_1();
    jj_save(0, xla);
    return retval;
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_2();
    jj_save(1, xla);
    return retval;
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_3();
    jj_save(2, xla);
    return retval;
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_4();
    jj_save(3, xla);
    return retval;
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_5();
    jj_save(4, xla);
    return retval;
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_6();
    jj_save(5, xla);
    return retval;
  }

  final private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_7();
    jj_save(6, xla);
    return retval;
  }

  final private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_8();
    jj_save(7, xla);
    return retval;
  }

  final private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_9();
    jj_save(8, xla);
    return retval;
  }

  final private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_10();
    jj_save(9, xla);
    return retval;
  }

  final private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_11();
    jj_save(10, xla);
    return retval;
  }

  final private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_12();
    jj_save(11, xla);
    return retval;
  }

  final private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_13();
    jj_save(12, xla);
    return retval;
  }

  final private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_14();
    jj_save(13, xla);
    return retval;
  }

  final private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_15();
    jj_save(14, xla);
    return retval;
  }

  final private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_16();
    jj_save(15, xla);
    return retval;
  }

  final private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_17();
    jj_save(16, xla);
    return retval;
  }

  final private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_18();
    jj_save(17, xla);
    return retval;
  }

  final private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_19();
    jj_save(18, xla);
    return retval;
  }

  final private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_20();
    jj_save(19, xla);
    return retval;
  }

  final private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_21();
    jj_save(20, xla);
    return retval;
  }

  final private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_22();
    jj_save(21, xla);
    return retval;
  }

  final private boolean jj_2_23(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_23();
    jj_save(22, xla);
    return retval;
  }

  final private boolean jj_2_24(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_24();
    jj_save(23, xla);
    return retval;
  }

  final private boolean jj_2_25(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_25();
    jj_save(24, xla);
    return retval;
  }

  final private boolean jj_2_26(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_26();
    jj_save(25, xla);
    return retval;
  }

  final private boolean jj_2_27(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_27();
    jj_save(26, xla);
    return retval;
  }

  final private boolean jj_3R_33() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_11()) {
    jj_scanpos = xsp;
    if (jj_3R_68()) {
    jj_scanpos = xsp;
    if (jj_3R_69()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_11() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_34()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_284() {
    if (jj_scan_token(BANG)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_264() {
    if (jj_scan_token(MOD)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_279() {
    if (jj_3R_207()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_278() {
    if (jj_3R_285()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_283() {
    if (jj_scan_token(TILDE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_271() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_277()) {
    jj_scanpos = xsp;
    if (jj_3R_278()) {
    jj_scanpos = xsp;
    if (jj_3R_279()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_277() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_283()) {
    jj_scanpos = xsp;
    if (jj_3R_284()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_244()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_263() {
    if (jj_scan_token(SLASH)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_239() {
    if (jj_scan_token(RSIGNEDSHIFTX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_206() {
    if (jj_scan_token(DECR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_262() {
    if (jj_scan_token(STAR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_205() {
    if (jj_scan_token(INCR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_245() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_262()) {
    jj_scanpos = xsp;
    if (jj_3R_263()) {
    jj_scanpos = xsp;
    if (jj_3R_264()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_244()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_238() {
    if (jj_scan_token(RSIGNEDSHIFT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_270() {
    if (jj_scan_token(MINUS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_231() {
    if (jj_scan_token(GEX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_261() {
    if (jj_3R_271()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_260() {
    if (jj_3R_206()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_259() {
    if (jj_3R_205()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_227() {
    if (jj_scan_token(GTX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_269() {
    if (jj_scan_token(PLUS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_244() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_258()) {
    jj_scanpos = xsp;
    if (jj_3R_259()) {
    jj_scanpos = xsp;
    if (jj_3R_260()) {
    jj_scanpos = xsp;
    if (jj_3R_261()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_258() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_269()) {
    jj_scanpos = xsp;
    if (jj_3R_270()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_244()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_247() {
    if (jj_scan_token(MINUS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_230() {
    if (jj_scan_token(GE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_234() {
    if (jj_3R_244()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_245()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_241() {
    if (jj_scan_token(RUNSIGNEDSHIFTX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_226() {
    if (jj_scan_token(GT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_237() {
    if (jj_scan_token(LSHIFTX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_246() {
    if (jj_scan_token(PLUS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_235() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_246()) {
    jj_scanpos = xsp;
    if (jj_3R_247()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_234()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_219() {
    if (jj_scan_token(NE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_222() {
    if (jj_3R_234()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_235()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_229() {
    if (jj_scan_token(LEX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_240() {
    if (jj_scan_token(RUNSIGNEDSHIFT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_225() {
    if (jj_scan_token(LTX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_236() {
    if (jj_scan_token(LSHIFT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_223() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_236()) {
    jj_scanpos = xsp;
    if (jj_3R_237()) {
    jj_scanpos = xsp;
    if (jj_3R_238()) {
    jj_scanpos = xsp;
    if (jj_3R_239()) {
    jj_scanpos = xsp;
    if (jj_3R_240()) {
    jj_scanpos = xsp;
    if (jj_3R_241()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_222()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_218() {
    if (jj_scan_token(EQ)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_211() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_218()) {
    jj_scanpos = xsp;
    if (jj_3R_219()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_210()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_220() {
    if (jj_3R_222()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_223()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_228() {
    if (jj_scan_token(LE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_224() {
    if (jj_scan_token(LT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_221() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_224()) {
    jj_scanpos = xsp;
    if (jj_3R_225()) {
    jj_scanpos = xsp;
    if (jj_3R_226()) {
    jj_scanpos = xsp;
    if (jj_3R_227()) {
    jj_scanpos = xsp;
    if (jj_3R_228()) {
    jj_scanpos = xsp;
    if (jj_3R_229()) {
    jj_scanpos = xsp;
    if (jj_3R_230()) {
    jj_scanpos = xsp;
    if (jj_3R_231()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_220()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_216() {
    if (jj_3R_220()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_221()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_217() {
    if (jj_scan_token(INSTANCEOF)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_210() {
    if (jj_3R_216()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_217()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_213() {
    if (jj_scan_token(BIT_ANDX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_195() {
    if (jj_scan_token(XOR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_194()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_203() {
    if (jj_3R_210()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_211()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_61() {
    if (jj_scan_token(ORASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_212() {
    if (jj_scan_token(BIT_AND)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_204() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_212()) {
    jj_scanpos = xsp;
    if (jj_3R_213()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_203()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_60() {
    if (jj_scan_token(XORASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_194() {
    if (jj_3R_203()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_204()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_197() {
    if (jj_scan_token(BIT_ORX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_59() {
    if (jj_scan_token(ANDASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_175() {
    if (jj_3R_194()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_195()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_65() {
    if (jj_scan_token(RSIGNEDSHIFTASSIGNX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_178() {
    if (jj_scan_token(BOOL_ANDX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_196() {
    if (jj_scan_token(BIT_OR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_107() {
    if (jj_scan_token(HOOK)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(COLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_85()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_176() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_196()) {
    jj_scanpos = xsp;
    if (jj_3R_197()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_175()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_58() {
    if (jj_scan_token(MINUSASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_64() {
    if (jj_scan_token(RSIGNEDSHIFTASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_147() {
    if (jj_3R_175()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_176()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_150() {
    if (jj_scan_token(BOOL_ORX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_177() {
    if (jj_scan_token(BOOL_AND)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_57() {
    if (jj_scan_token(PLUSASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_148() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_177()) {
    jj_scanpos = xsp;
    if (jj_3R_178()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_147()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_125() {
    if (jj_3R_147()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_148()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_56() {
    if (jj_scan_token(MODASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_149() {
    if (jj_scan_token(BOOL_OR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_126() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_149()) {
    jj_scanpos = xsp;
    if (jj_3R_150()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_125()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_106() {
    if (jj_3R_125()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_126()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_55() {
    if (jj_scan_token(SLASHASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_67() {
    if (jj_scan_token(RUNSIGNEDSHIFTASSIGNX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_63() {
    if (jj_scan_token(LSHIFTASSIGNX)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_85() {
    if (jj_3R_106()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_107()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_54() {
    if (jj_scan_token(STARASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_66() {
    if (jj_scan_token(RUNSIGNEDSHIFTASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_62() {
    if (jj_scan_token(LSHIFTASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_53() {
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_32() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_53()) {
    jj_scanpos = xsp;
    if (jj_3R_54()) {
    jj_scanpos = xsp;
    if (jj_3R_55()) {
    jj_scanpos = xsp;
    if (jj_3R_56()) {
    jj_scanpos = xsp;
    if (jj_3R_57()) {
    jj_scanpos = xsp;
    if (jj_3R_58()) {
    jj_scanpos = xsp;
    if (jj_3R_59()) {
    jj_scanpos = xsp;
    if (jj_3R_60()) {
    jj_scanpos = xsp;
    if (jj_3R_61()) {
    jj_scanpos = xsp;
    if (jj_3R_62()) {
    jj_scanpos = xsp;
    if (jj_3R_63()) {
    jj_scanpos = xsp;
    if (jj_3R_64()) {
    jj_scanpos = xsp;
    if (jj_3R_65()) {
    jj_scanpos = xsp;
    if (jj_3R_66()) {
    jj_scanpos = xsp;
    if (jj_3R_67()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_9() {
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_32()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_84() {
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_32()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_45() {
    if (jj_3R_85()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_26() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_44()) {
    jj_scanpos = xsp;
    if (jj_3R_45()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_44() {
    if (jj_3R_84()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_8() {
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_28() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_8()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_77() {
    if (jj_scan_token(DOUBLE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_209() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_76() {
    if (jj_scan_token(FLOAT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_75() {
    if (jj_scan_token(LONG)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_74() {
    if (jj_scan_token(INT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_73() {
    if (jj_scan_token(SHORT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_50() {
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_72() {
    if (jj_scan_token(BYTE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_71() {
    if (jj_scan_token(CHAR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_70() {
    if (jj_scan_token(BOOLEAN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_34() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_70()) {
    jj_scanpos = xsp;
    if (jj_3R_71()) {
    jj_scanpos = xsp;
    if (jj_3R_72()) {
    jj_scanpos = xsp;
    if (jj_3R_73()) {
    jj_scanpos = xsp;
    if (jj_3R_74()) {
    jj_scanpos = xsp;
    if (jj_3R_75()) {
    jj_scanpos = xsp;
    if (jj_3R_76()) {
    jj_scanpos = xsp;
    if (jj_3R_77()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_87() {
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_86() {
    if (jj_scan_token(VOID)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_46() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_86()) {
    jj_scanpos = xsp;
    if (jj_3R_87()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_7() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_49() {
    if (jj_3R_34()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_30() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_49()) {
    jj_scanpos = xsp;
    if (jj_3R_50()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_7()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_142() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_141()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_171() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_141() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3R_171()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_257() {
    if (jj_scan_token(FINALLY)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_256() {
    if (jj_scan_token(CATCH)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_141()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_5() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_29()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_192() {
    if (jj_scan_token(TRY)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_256()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    xsp = jj_scanpos;
    if (jj_3R_257()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_118() {
    if (jj_3R_141()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_142()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_255() {
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_100() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_118()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_208() {
    if (jj_3R_29()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_5()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_254() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_191() {
    if (jj_scan_token(THROW)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_233() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_232()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_88() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_208()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    xsp = jj_scanpos;
    if (jj_3R_209()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_190() {
    if (jj_scan_token(RETURN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_255()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_282() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_182()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_253() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_48() {
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_189() {
    if (jj_scan_token(CONTINUE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_254()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_29() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_47()) {
    jj_scanpos = xsp;
    if (jj_3R_48()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_47() {
    if (jj_3R_88()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_242() {
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_29()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_243() {
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(STAR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_188() {
    if (jj_scan_token(BREAK)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_253()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_268() {
    if (jj_3R_276()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_276() {
    if (jj_3R_182()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_282()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_232() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_242()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_156() {
    if (jj_scan_token(FINAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_170() {
    if (jj_scan_token(IMPORT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(STAR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_137() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_156()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_232()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_233()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_43() {
    if (jj_scan_token(FINAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_27() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_43()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_139() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3R_170()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_scan_token(IMPORT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_243()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_249() {
    if (jj_scan_token(ELSE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_3() {
    if (jj_3R_27()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_27() {
    if (jj_3R_46()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_275() {
    if (jj_3R_276()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_250() {
    if (jj_3R_267()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_252() {
    if (jj_3R_268()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_267() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_274()) {
    jj_scanpos = xsp;
    if (jj_3R_275()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_274() {
    if (jj_3R_137()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_251() {
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_81() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_100()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_80() {
    if (jj_3R_46()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_100()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_40() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_80()) {
    jj_scanpos = xsp;
    if (jj_3R_81()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_3R_27()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_187() {
    if (jj_scan_token(FOR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_250()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    xsp = jj_scanpos;
    if (jj_3R_251()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    xsp = jj_scanpos;
    if (jj_3R_252()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_155() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_100()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_154() {
    if (jj_3R_46()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_100()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_186() {
    if (jj_scan_token(DO)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(WHILE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_136() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_154()) {
    jj_scanpos = xsp;
    if (jj_3R_155()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_185() {
    if (jj_scan_token(WHILE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_184() {
    if (jj_scan_token(IF)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_249()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_266() {
    if (jj_3R_99()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_273() {
    if (jj_scan_token(_DEFAULT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(COLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_265() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_272()) {
    jj_scanpos = xsp;
    if (jj_3R_273()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_272() {
    if (jj_scan_token(CASE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(COLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_248() {
    if (jj_3R_265()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_266()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3_26() {
    if (jj_3R_42()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_32()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_183() {
    if (jj_scan_token(SWITCH)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_248()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    if (jj_scan_token(RBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_201() {
    if (jj_3R_207()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_200() {
    if (jj_3R_84()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_199() {
    if (jj_3R_206()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_182() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_198()) {
    jj_scanpos = xsp;
    if (jj_3R_199()) {
    jj_scanpos = xsp;
    if (jj_3R_200()) {
    jj_scanpos = xsp;
    if (jj_3R_201()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_198() {
    if (jj_3R_205()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_181() {
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_140() {
    if (jj_scan_token(FORMAL_COMMENT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_41() {
    if (jj_scan_token(FINAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_117() {
    if (jj_3R_140()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_25() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_41()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_116() {
    if (jj_3R_139()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_24() {
    if (jj_3R_40()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_115() {
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_114() {
    if (jj_3R_137()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_113() {
    if (jj_3R_136()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_99() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_113()) {
    jj_scanpos = xsp;
    if (jj_3R_114()) {
    jj_scanpos = xsp;
    if (jj_3R_115()) {
    jj_scanpos = xsp;
    if (jj_3R_116()) {
    jj_scanpos = xsp;
    if (jj_3R_117()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_79() {
    if (jj_3R_99()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_38() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_79()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    if (jj_scan_token(RBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_39() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(COLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_138()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_169() {
    if (jj_3R_192()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_168() {
    if (jj_3R_191()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_167() {
    if (jj_3R_190()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_166() {
    if (jj_3R_189()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_165() {
    if (jj_3R_188()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_164() {
    if (jj_3R_187()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_163() {
    if (jj_3R_186()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_162() {
    if (jj_3R_185()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_161() {
    if (jj_3R_184()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_160() {
    if (jj_3R_183()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_19() {
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_159() {
    if (jj_3R_182()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(SEMICOLON)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_158() {
    if (jj_3R_181()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_157() {
    if (jj_3R_38()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_138() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_23()) {
    jj_scanpos = xsp;
    if (jj_3R_157()) {
    jj_scanpos = xsp;
    if (jj_3R_158()) {
    jj_scanpos = xsp;
    if (jj_3R_159()) {
    jj_scanpos = xsp;
    if (jj_3R_160()) {
    jj_scanpos = xsp;
    if (jj_3R_161()) {
    jj_scanpos = xsp;
    if (jj_3R_162()) {
    jj_scanpos = xsp;
    if (jj_3R_163()) {
    jj_scanpos = xsp;
    if (jj_3R_164()) {
    jj_scanpos = xsp;
    if (jj_3R_165()) {
    jj_scanpos = xsp;
    if (jj_3R_166()) {
    jj_scanpos = xsp;
    if (jj_3R_167()) {
    jj_scanpos = xsp;
    if (jj_3R_168()) {
    jj_scanpos = xsp;
    if (jj_3R_169()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_23() {
    if (jj_3R_39()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_174() {
    if (jj_3R_78()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_19()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_202() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_173() {
    if (jj_3R_172()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_193() {
    Token xsp;
    if (jj_3R_202()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_202()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    if (jj_3R_88()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_21() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_22() {
    Token xsp;
    if (jj_3_21()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_21()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_172() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3R_193()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_143() {
    if (jj_scan_token(NEW)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_173()) {
    jj_scanpos = xsp;
    if (jj_3R_174()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_119() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_20()) {
    jj_scanpos = xsp;
    if (jj_3R_143()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_20() {
    if (jj_scan_token(NEW)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_34()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_172()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_135() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_112() {
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_135()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_98() {
    if (jj_3R_112()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_78() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_98()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_153() {
    if (jj_scan_token(VOID)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_152() {
    if (jj_scan_token(NULL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_180() {
    if (jj_scan_token(FALSE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_151() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_179()) {
    jj_scanpos = xsp;
    if (jj_3R_180()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_179() {
    if (jj_scan_token(TRUE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_134() {
    if (jj_3R_153()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_133() {
    if (jj_3R_152()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_132() {
    if (jj_3R_151()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_131() {
    if (jj_scan_token(STRING_LITERAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_130() {
    if (jj_scan_token(CHARACTER_LITERAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_129() {
    if (jj_scan_token(FLOATING_POINT_LITERAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_128() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_111() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_128()) {
    jj_scanpos = xsp;
    if (jj_3R_129()) {
    jj_scanpos = xsp;
    if (jj_3R_130()) {
    jj_scanpos = xsp;
    if (jj_3R_131()) {
    jj_scanpos = xsp;
    if (jj_3R_132()) {
    jj_scanpos = xsp;
    if (jj_3R_133()) {
    jj_scanpos = xsp;
    if (jj_3R_134()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_127() {
    if (jj_3R_78()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_110() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_52() {
    if (jj_3R_91()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_109() {
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_127()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_108() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_91() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_108()) {
    jj_scanpos = xsp;
    if (jj_3R_109()) {
    jj_scanpos = xsp;
    if (jj_3R_110()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_18() {
    if (jj_3R_37()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_31() {
    if (jj_3R_51()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_52()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_90() {
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_89() {
    if (jj_3R_37()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_51() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_89()) {
    jj_scanpos = xsp;
    if (jj_3R_90()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_146() {
    if (jj_3R_78()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_97() {
    if (jj_3R_111()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_124() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_96() {
    if (jj_scan_token(NEW)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_123() {
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_146()) jj_scanpos = xsp;
    else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_122() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_105() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_17()) {
    jj_scanpos = xsp;
    if (jj_3R_122()) {
    jj_scanpos = xsp;
    if (jj_3R_123()) {
    jj_scanpos = xsp;
    if (jj_3R_124()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_17() {
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(CLASS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_15() {
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(DOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(CLASS)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_145() {
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_16() {
    if (jj_3R_37()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_144() {
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_121() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_144()) {
    jj_scanpos = xsp;
    if (jj_3R_145()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_120() {
    if (jj_3R_37()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_36() {
    if (jj_scan_token(DECR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_95() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_83() {
    if (jj_3R_105()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_104() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_120()) {
    jj_scanpos = xsp;
    if (jj_3R_121()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_35() {
    if (jj_scan_token(INCR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_103() {
    if (jj_3R_119()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_102() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_26()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_94() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_101() {
    if (jj_3R_111()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_82() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_101()) {
    jj_scanpos = xsp;
    if (jj_3R_102()) {
    jj_scanpos = xsp;
    if (jj_3R_103()) {
    jj_scanpos = xsp;
    if (jj_3R_104()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_93() {
    if (jj_scan_token(BANG)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_37() {
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_78()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_14() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_34()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_281() {
    if (jj_scan_token(DECR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_92() {
    if (jj_scan_token(TILDE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_42() {
    if (jj_3R_82()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_83()) { jj_scanpos = xsp; break; }
      if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    }
    return false;
  }

  final private boolean jj_3R_287() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_271()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_286() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_30()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_244()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_280() {
    if (jj_scan_token(INCR)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_13() {
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_35()) {
    jj_scanpos = xsp;
    if (jj_3R_36()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_285() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_286()) {
    jj_scanpos = xsp;
    if (jj_3R_287()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_215() {
    if (jj_3R_42()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_12() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_207() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_214()) {
    jj_scanpos = xsp;
    if (jj_3R_215()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_214() {
    if (jj_3R_31()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_280()) {
    jj_scanpos = xsp;
    if (jj_3R_281()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_69() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_92()) {
    jj_scanpos = xsp;
    if (jj_3R_93()) {
    jj_scanpos = xsp;
    if (jj_3R_94()) {
    jj_scanpos = xsp;
    if (jj_3R_95()) {
    jj_scanpos = xsp;
    if (jj_3R_96()) {
    jj_scanpos = xsp;
    if (jj_3R_97()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_10() {
    if (jj_3R_33()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_68() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_28()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(RBRACKET)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  public ParserTokenManager token_source;
  ASCII_UCodeESC_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[88];
  final private int[] jj_la1_0 = {0x6db52c00,0x6db52c01,0x0,0x0,0x0,0x40000000,0x0,0x4a12400,0x4a12400,0x0,0x0,0x4212400,0x0,0x4212400,0x4212400,0x4212400,0x4a12400,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x80000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4a12400,0x0,0x0,0x4a12400,0x800000,0x0,0x0,0x4a12400,0x0,0x0,0x0,0x4212400,0x4a12400,0x0,0x0,0x0,0x0,0x0,0x0,0x800000,0x800000,0x4a12400,0x0,0x0,0x0,0x0,0x0,0x2cb52c00,0x6db52c00,0x6cb52c00,0x0,0x4a12400,0x84000,0x6db52c00,0x84000,0x400000,0x5a12400,0x4a12400,0x4a12400,0x4a12400,0x1000000,0x0,0x0,0x0,0x0,0x4a12400,0x8000,0x2000000,};
  final private int[] jj_la1_1 = {0xa7a3fb1d,0xa7a3fb1d,0x4000000,0x4000000,0x0,0x0,0x0,0xa5a2a21d,0xa5a2a21d,0x0,0x0,0x4000205,0x4000000,0x4000205,0x4008205,0x205,0x25a2a21d,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x25a2a21d,0x0,0x0,0x25a2a21d,0x25a2a018,0x20000000,0x0,0x25a2a21d,0x20000000,0x80000000,0x4000000,0x4000205,0x25a2a21d,0x20000000,0x80000000,0x4000000,0x80000000,0x20000000,0x80000000,0x1a2a010,0x2000,0x25a2a21d,0x0,0x20000000,0x8,0x0,0x0,0xa5a3fb1d,0xa7a3fb1d,0xa7a3fb1d,0x0,0x25a2a21d,0x0,0xa7a3fb1d,0x0,0x0,0x25a2a21d,0x25a2a21d,0x25a2a21d,0x25a2a21d,0x0,0x0,0x0,0x4000000,0x4000000,0x25a2a21d,0x0,0x0,};
  final private int[] jj_la1_2 = {0x6000008,0x1e001808,0x0,0x0,0x20,0x0,0x40,0x1e001800,0x1e001800,0x10,0x10,0x0,0x0,0x0,0x0,0x0,0x1e001800,0x40,0x2000,0x600000,0x600000,0x1800000,0x1800000,0x0,0x0,0x0,0x80000000,0x80000000,0x108000,0x108000,0x0,0xf0780,0xf0780,0x0,0x0,0x18000000,0x18000000,0x60000000,0x60000000,0x18000000,0x1e001800,0x1800,0x1800,0x0,0x1800,0x0,0x6000000,0x0,0x0,0x22,0x0,0x0,0x0,0x0,0x22,0x0,0x22,0x0,0x22,0x0,0x0,0x1e001800,0x10,0x2,0x0,0x2,0x2,0x6000008,0x6000008,0x6000008,0x6000000,0x0,0x0,0x6000008,0x0,0x0,0x6000000,0x1e001800,0x6000000,0x6000000,0x0,0x10,0x10,0x0,0x0,0x1e001800,0x0,0x0,};
  final private int[] jj_la1_3 = {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x7faf800,0x0,0x0,0x0,0x0,0x0,0x6,0x6,0x8,0x1,0x1,0x0,0x0,0x0,0x0,0x0,0x7e0,0x7e0,0x0,0x0,0x10,0x10,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
  final private JJCalls[] jj_2_rtns = new JJCalls[27];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public Parser(java.io.InputStream stream) {
    jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public Parser(java.io.Reader stream) {
    jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 88; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
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

  final private boolean jj_scan_token(int kind) {
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
    return (jj_scanpos.kind != kind);
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
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
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[123];
    for (int i = 0; i < 123; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 88; i++) {
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
          if ((jj_la1_3[i] & (1<<j)) != 0) {
            la1tokens[96+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 123; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 27; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
            case 11: jj_3_12(); break;
            case 12: jj_3_13(); break;
            case 13: jj_3_14(); break;
            case 14: jj_3_15(); break;
            case 15: jj_3_16(); break;
            case 16: jj_3_17(); break;
            case 17: jj_3_18(); break;
            case 18: jj_3_19(); break;
            case 19: jj_3_20(); break;
            case 20: jj_3_21(); break;
            case 21: jj_3_22(); break;
            case 22: jj_3_23(); break;
            case 23: jj_3_24(); break;
            case 24: jj_3_25(); break;
            case 25: jj_3_26(); break;
            case 26: jj_3_27(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
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
