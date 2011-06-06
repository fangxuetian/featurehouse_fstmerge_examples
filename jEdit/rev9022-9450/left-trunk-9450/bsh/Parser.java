
package bsh;

import java.io.*;
import java.util.Vector;


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

        
        boolean isRegularForStatement()
        {
                int curTok = 1;
                Token tok;
                tok = getToken(curTok++);
                if ( tok.kind != FOR ) return false;
                tok = getToken(curTok++);
                if ( tok.kind != LPAREN ) return false;
                while (true)
                {
                        tok = getToken(curTok++);
                        switch (tok.kind) {
                                case COLON:
                                        return false;
                                case SEMICOLON:
                                        return true;
                                case EOF:
                                        return false;
                        }
                }
        }

        
        ParseException createParseException( String message )
        {
                Token errortok = token;
                int line = errortok.beginLine, column = errortok.beginColumn;
                String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
                return new ParseException( "Parse error at line " + line
                        + ", column " + column + " : " + message );
        }


  final public boolean Line() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 0:
      jj_consume_token(0);
        Interpreter.debug("End of File!");
        {if (true) return true;}
      break;
    default:
      if (jj_2_1(1)) {
        BlockStatement();
        {if (true) return false;}
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }




  final public Modifiers Modifiers(int context, boolean lookahead) throws ParseException {
        Modifiers mods = null;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case FINAL:
      case NATIVE:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case VOLATILE:
        ;
        break;
      default:
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PRIVATE:
        jj_consume_token(PRIVATE);
        break;
      case PROTECTED:
        jj_consume_token(PROTECTED);
        break;
      case PUBLIC:
        jj_consume_token(PUBLIC);
        break;
      case SYNCHRONIZED:
        jj_consume_token(SYNCHRONIZED);
        break;
      case FINAL:
        jj_consume_token(FINAL);
        break;
      case NATIVE:
        jj_consume_token(NATIVE);
        break;
      case TRANSIENT:
        jj_consume_token(TRANSIENT);
        break;
      case VOLATILE:
        jj_consume_token(VOLATILE);
        break;
      case ABSTRACT:
        jj_consume_token(ABSTRACT);
        break;
      case STATIC:
        jj_consume_token(STATIC);
        break;
      case STRICTFP:
        jj_consume_token(STRICTFP);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
                if ( !lookahead )
                        try {
                                if ( mods == null ) mods = new Modifiers();
                                mods.addModifier( context, getToken(0).image );
                        } catch ( IllegalStateException e ) {
                                {if (true) throw createParseException( e.getMessage() );}
                        }
    }
        {if (true) return mods;}
    throw new Error("Missing return statement in function");
  }


  final public void ClassDeclaration() throws ParseException {
 
        BSHClassDeclaration jjtn000 = new BSHClassDeclaration(JJTCLASSDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);Modifiers mods;
        Token name;
        int numInterfaces;
    try {
      mods = Modifiers(Modifiers.CLASS, false);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        jj_consume_token(CLASS);
        break;
      case INTERFACE:
        jj_consume_token(INTERFACE);
                                  jjtn000.isInterface=true;
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      name = jj_consume_token(IDENTIFIER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EXTENDS:
        jj_consume_token(EXTENDS);
        AmbiguousName();
                                      jjtn000.extend = true;
        break;
      default:
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IMPLEMENTS:
        jj_consume_token(IMPLEMENTS);
        numInterfaces = NameList();
                  jjtn000.numInterfaces=numInterfaces;
        break;
      default:
        ;
      }
      Block();
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
                jjtn000.modifiers = mods;
                jjtn000.name = name.image;
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

  final public void MethodDeclaration() throws ParseException {
 
        BSHMethodDeclaration jjtn000 = new BSHMethodDeclaration(JJTMETHODDECLARATION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);Token t = null;
        Modifiers mods;
        int count;
    try {
      mods = Modifiers(Modifiers.METHOD, false);
                                                      jjtn000.modifiers = mods;
      if (jj_2_2(2147483647)) {
        t = jj_consume_token(IDENTIFIER);
                           jjtn000.name = t.image;
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
        case VOID:
        case IDENTIFIER:
          ReturnType();
          t = jj_consume_token(IDENTIFIER);
                           jjtn000.name = t.image;
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      FormalParameters();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case THROWS:
        jj_consume_token(THROWS);
        count = NameList();
                                      jjtn000.numThrows=count;
        break;
      default:
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        Block();
        break;
      case SEMICOLON:
        jj_consume_token(SEMICOLON);
        break;
      default:
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

  final public void PackageDeclaration() throws ParseException {
 
  BSHPackageDeclaration jjtn000 = new BSHPackageDeclaration(JJTPACKAGEDECLARATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(PACKAGE);
      AmbiguousName();
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

  final public void ImportDeclaration() throws ParseException {
 
    BSHImportDeclaration jjtn000 = new BSHImportDeclaration(JJTIMPORTDECLARATION);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token s = null;
        Token t = null;
    try {
      if (jj_2_3(3)) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case STATIC:
          s = jj_consume_token(STATIC);
          break;
        default:
          ;
        }
        jj_consume_token(IMPORT);
        AmbiguousName();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DOT:
          t = jj_consume_token(DOT);
          jj_consume_token(STAR);
          break;
        default:
          ;
        }
        jj_consume_token(SEMICOLON);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtreeCloseNodeScope(jjtn000);
                if ( s != null ) jjtn000.staticImport = true;
                if ( t != null ) jjtn000.importPackage = true;
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
        label_2:
        while (true) {
          if (jj_2_4(2)) {
            ;
          } else {
            break label_2;
          }
          jj_consume_token(COMMA);
          VariableInitializer();
        }
        break;
      default:
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        jj_consume_token(COMMA);
        break;
      default:
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
        label_3:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COMMA:
            ;
            break;
          default:
            break label_3;
          }
          jj_consume_token(COMMA);
          FormalParameter();
        }
        break;
      default:
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
      if (jj_2_5(2)) {
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
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_4:
      while (true) {
        if (jj_2_6(2)) {
          ;
        } else {
          break label_4;
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
      label_5:
      while (true) {
        if (jj_2_7(2)) {
          ;
        } else {
          break label_5;
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

  final public int NameList() throws ParseException {
  int count = 0;
    AmbiguousName();
                    ++count;
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_6;
      }
      jj_consume_token(COMMA);
      AmbiguousName();
                                                       ++count;
    }
    {if (true) return count;}
    throw new Error("Missing return statement in function");
  }


  final public void Expression() throws ParseException {
    if (jj_2_8(2147483647)) {
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
      PrimaryExpression();
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
      ;
    }
  }

  final public void ConditionalOrExpression() throws ParseException {
  Token t=null;
    ConditionalAndExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_OR:
      case BOOL_ORX:
        ;
        break;
      default:
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_OR:
        t = jj_consume_token(BOOL_OR);
        break;
      case BOOL_ORX:
        t = jj_consume_token(BOOL_ORX);
        break;
      default:
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
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_AND:
      case BOOL_ANDX:
        ;
        break;
      default:
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOL_AND:
        t = jj_consume_token(BOOL_AND);
        break;
      case BOOL_ANDX:
        t = jj_consume_token(BOOL_ANDX);
        break;
      default:
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
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_OR:
      case BIT_ORX:
        ;
        break;
      default:
        break label_9;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_OR:
        t = jj_consume_token(BIT_OR);
        break;
      case BIT_ORX:
        t = jj_consume_token(BIT_ORX);
        break;
      default:
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
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case XOR:
        ;
        break;
      default:
        break label_10;
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
    label_11:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
      case BIT_ANDX:
        ;
        break;
      default:
        break label_11;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
        t = jj_consume_token(BIT_AND);
        break;
      case BIT_ANDX:
        t = jj_consume_token(BIT_ANDX);
        break;
      default:
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
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
      case NE:
        ;
        break;
      default:
        break label_12;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
        t = jj_consume_token(EQ);
        break;
      case NE:
        t = jj_consume_token(NE);
        break;
      default:
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
      ;
    }
  }

  final public void RelationalExpression() throws ParseException {
  Token t = null;
    ShiftExpression();
    label_13:
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
        break label_13;
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
    label_14:
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
        break label_14;
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
    label_15:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        break label_15;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        t = jj_consume_token(PLUS);
        break;
      case MINUS:
        t = jj_consume_token(MINUS);
        break;
      default:
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
    label_16:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
      case SLASH:
      case MOD:
        ;
        break;
      default:
        break label_16;
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
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void PreIncrementExpression() throws ParseException {
  Token t = null;
    t = jj_consume_token(INCR);
    PrimaryExpression();
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
    PrimaryExpression();
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
      if (jj_2_9(2147483647)) {
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
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }


  final public void CastLookahead() throws ParseException {
    if (jj_2_10(2)) {
      jj_consume_token(LPAREN);
      PrimitiveType();
    } else if (jj_2_11(2147483647)) {
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
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void PostfixExpression() throws ParseException {
  Token t = null;
    if (jj_2_12(2147483647)) {
      PrimaryExpression();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INCR:
        t = jj_consume_token(INCR);
        break;
      case DECR:
        t = jj_consume_token(DECR);
        break;
      default:
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
      if (jj_2_13(2147483647)) {
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
      label_17:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case LBRACKET:
        case DOT:
          ;
          break;
        default:
          break label_17;
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
    default:
      if (jj_2_14(2147483647)) {
        MethodInvocation();
      } else if (jj_2_15(2147483647)) {
        Type();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
          AmbiguousName();
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

  final public void PrimarySuffix() throws ParseException {
 
    BSHPrimarySuffix jjtn000 = new BSHPrimarySuffix(JJTPRIMARYSUFFIX);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);
    jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      if (jj_2_16(2)) {
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

        
        
        jjtn000.value = new Primitive( new Long( literal ).longValue() );
    }
    else
                try {
                jjtn000.value = new Primitive(
                                Integer.decode( literal ).intValue() );
                } catch ( NumberFormatException e ) {
                        {if (true) throw createParseException(
                                "Error or number too big for integer type: "+ literal );}
                }
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
        jjtn000.value = new Primitive( new Float( literal ).floatValue() );
    }
    else
    {
        if(ch == 'd' || ch == 'D')
            literal = literal.substring(0,literal.length()-1);

        jjtn000.value = new Primitive( new Double( literal ).doubleValue() );
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
                        {if (true) throw createParseException("Error parsing character: "+x.image);}
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
                        {if (true) throw createParseException("Error parsing string: "+x.image);}
                }
        break;
      case FALSE:
      case TRUE:
        b = BooleanLiteral();
                          jjtree.closeNodeScope(jjtn000, true);
                          jjtc000 = false;
                          jjtreeCloseNodeScope(jjtn000);
    jjtn000.value = new Primitive( b );
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
    label_18:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_18;
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
      if (jj_2_18(2)) {
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
            if (jj_2_17(2)) {
              Block();
            } else {
              ;
            }
            break;
          default:
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
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
      if (jj_2_21(2)) {
        label_19:
        while (true) {
          jj_consume_token(LBRACKET);
          Expression();
          jj_consume_token(RBRACKET);
                                        jjtn000.addDefinedDimension();
          if (jj_2_19(2)) {
            ;
          } else {
            break label_19;
          }
        }
        label_20:
        while (true) {
          if (jj_2_20(2)) {
            ;
          } else {
            break label_20;
          }
          jj_consume_token(LBRACKET);
          jj_consume_token(RBRACKET);
                           jjtn000.addUndefinedDimension();
        }
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACKET:
          label_21:
          while (true) {
            jj_consume_token(LBRACKET);
            jj_consume_token(RBRACKET);
              jjtn000.addUndefinedDimension();
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case LBRACKET:
              ;
              break;
            default:
              break label_21;
            }
          }
          ArrayInitializer();
          break;
        default:
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
    if (jj_2_22(2)) {
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
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
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
      default:
        if (isRegularForStatement()) {
          ForStatement();
        } else {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case FOR:
            EnhancedForStatement();
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
          case SYNCHRONIZED:
            SynchronizedStatement();
            break;
          case THROW:
            ThrowStatement();
            break;
          case TRY:
            TryStatement();
            break;
          default:
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
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
      label_22:
      while (true) {
        if (jj_2_23(1)) {
          ;
        } else {
          break label_22;
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
      ClassDeclaration();
    } else if (jj_2_25(2147483647)) {
      MethodDeclaration();
    } else if (jj_2_26(2147483647)) {
      MethodDeclaration();
    } else if (jj_2_27(2147483647)) {
      TypedVariableDeclaration();
      jj_consume_token(SEMICOLON);
    } else if (jj_2_28(1)) {
      Statement();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IMPORT:
      case STATIC:
        ImportDeclaration();
        break;
      case PACKAGE:
        PackageDeclaration();
        break;
      case FORMAL_COMMENT:
        FormalComment();
        break;
      default:
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
    Expression();
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
      label_23:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CASE:
        case _DEFAULT:
          ;
          break;
        default:
          break label_23;
        }
        SwitchLabel();
        label_24:
        while (true) {
          if (jj_2_29(1)) {
            ;
          } else {
            break label_24;
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
  jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      jj_consume_token(FOR);
      jj_consume_token(LPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FINAL:
      case FLOAT:
      case INT:
      case LONG:
      case NATIVE:
      case NEW:
      case NULL:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case SHORT:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case TRUE:
      case VOID:
      case VOLATILE:
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
        ForInit();
                          jjtn000.hasForInit=true;
        break;
      default:
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
        ForUpdate();
                        jjtn000.hasForUpdate=true;
        break;
      default:
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


  final public void EnhancedForStatement() throws ParseException {
 
  BSHEnhancedForStatement jjtn000 = new BSHEnhancedForStatement(JJTENHANCEDFORSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);Token t = null;
    try {
      if (jj_2_30(4)) {
        jj_consume_token(FOR);
        jj_consume_token(LPAREN);
        t = jj_consume_token(IDENTIFIER);
        jj_consume_token(COLON);
        Expression();
        jj_consume_token(RPAREN);
        Statement();
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                  jjtreeCloseNodeScope(jjtn000);
                  jjtn000.varName = t.image;
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case FOR:
          jj_consume_token(FOR);
          jj_consume_token(LPAREN);
          Type();
          t = jj_consume_token(IDENTIFIER);
          jj_consume_token(COLON);
          Expression();
          jj_consume_token(RPAREN);
          Statement();
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                  jjtreeCloseNodeScope(jjtn000);
                  jjtn000.varName = t.image;
          break;
        default:
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

  final public void ForInit() throws ParseException {
  Token t = null;
    if (jj_2_31(2147483647)) {
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
      case BANG:
      case TILDE:
      case INCR:
      case DECR:
      case PLUS:
      case MINUS:
        StatementExpressionList();
        break;
      default:
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
        Modifiers mods;
    try {
      mods = Modifiers(Modifiers.FIELD, false);
      Type();
      VariableDeclarator();
      label_25:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          break label_25;
        }
        jj_consume_token(COMMA);
        VariableDeclarator();
      }
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
        jjtn000.modifiers = mods;
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
      label_26:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          break label_26;
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

  final public void SynchronizedStatement() throws ParseException {
 
  BSHBlock jjtn000 = new BSHBlock(JJTBLOCK);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
  jjtreeOpenNodeScope(jjtn000);
    try {
      jj_consume_token(SYNCHRONIZED);
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      Block();
                                                jjtree.closeNodeScope(jjtn000, true);
                                                jjtc000 = false;
                                                jjtreeCloseNodeScope(jjtn000);
                jjtn000.isSynchronized=true;
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
  jjtreeOpenNodeScope(jjtn000);boolean closed = false;
    try {
      jj_consume_token(TRY);
      Block();
      label_27:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CATCH:
          ;
          break;
        default:
          break label_27;
        }
        jj_consume_token(CATCH);
        jj_consume_token(LPAREN);
        FormalParameter();
        jj_consume_token(RPAREN);
        Block();
                                                      closed = true;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FINALLY:
        jj_consume_token(FINALLY);
        Block();
                              closed = true;
        break;
      default:
        ;
      }
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          jjtreeCloseNodeScope(jjtn000);
                if ( !closed ) {if (true) throw generateParseException();}
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
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_23(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_24(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_25(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_26(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_27(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_28(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_29(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_29(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_30(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_30(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_31(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_31(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_3R_46() {
    if (jj_3R_91()) return true;
    return false;
  }

  final private boolean jj_3R_28() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_46()) {
    jj_scanpos = xsp;
    if (jj_3R_47()) {
    jj_scanpos = xsp;
    if (jj_3R_48()) {
    jj_scanpos = xsp;
    if (jj_3R_49()) {
    jj_scanpos = xsp;
    if (jj_3_28()) {
    jj_scanpos = xsp;
    if (jj_3R_50()) {
    jj_scanpos = xsp;
    if (jj_3R_51()) {
    jj_scanpos = xsp;
    if (jj_3R_52()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3_23() {
    if (jj_3R_28()) return true;
    return false;
  }

  final private boolean jj_3R_161() {
    if (jj_3R_164()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_169()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_38() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_23()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_158() {
    if (jj_3R_161()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_167()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_40() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_156() {
    if (jj_scan_token(HOOK)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_108()) return true;
    return false;
  }

  final private boolean jj_3R_165() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(108)) {
    jj_scanpos = xsp;
    if (jj_scan_token(109)) return true;
    }
    if (jj_3R_158()) return true;
    return false;
  }

  final private boolean jj_3R_153() {
    if (jj_3R_158()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_165()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_90() {
    if (jj_3R_124()) return true;
    return false;
  }

  final private boolean jj_3R_89() {
    if (jj_3R_123()) return true;
    return false;
  }

  final private boolean jj_3R_88() {
    if (jj_3R_122()) return true;
    return false;
  }

  final private boolean jj_3R_162() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(98)) {
    jj_scanpos = xsp;
    if (jj_scan_token(99)) return true;
    }
    if (jj_3R_153()) return true;
    return false;
  }

  final private boolean jj_3R_87() {
    if (jj_3R_121()) return true;
    return false;
  }

  final private boolean jj_3R_148() {
    if (jj_3R_153()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_162()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_86() {
    if (jj_3R_120()) return true;
    return false;
  }

  final private boolean jj_3R_85() {
    if (jj_3R_119()) return true;
    return false;
  }

  final private boolean jj_3R_84() {
    if (jj_3R_118()) return true;
    return false;
  }

  final private boolean jj_3R_159() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(96)) {
    jj_scanpos = xsp;
    if (jj_scan_token(97)) return true;
    }
    if (jj_3R_148()) return true;
    return false;
  }

  final private boolean jj_3R_83() {
    if (jj_3R_117()) return true;
    return false;
  }

  final private boolean jj_3R_135() {
    if (jj_3R_148()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_159()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_82() {
    if (jj_3R_116()) return true;
    return false;
  }

  final private boolean jj_3R_81() {
    if (jj_3R_115()) return true;
    return false;
  }

  final private boolean jj_3R_80() {
    if (jj_3R_114()) return true;
    return false;
  }

  final private boolean jj_3R_108() {
    if (jj_3R_135()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_156()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_79() {
    if (jj_3R_113()) return true;
    return false;
  }

  final private boolean jj_3R_78() {
    if (jj_3R_112()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3_17() {
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3R_77() {
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3R_45() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3R_77()) {
    jj_scanpos = xsp;
    if (jj_scan_token(78)) {
    jj_scanpos = xsp;
    if (jj_3R_78()) {
    jj_scanpos = xsp;
    if (jj_3R_79()) {
    jj_scanpos = xsp;
    if (jj_3R_80()) {
    jj_scanpos = xsp;
    if (jj_3R_81()) {
    jj_scanpos = xsp;
    if (jj_3R_82()) {
    jj_scanpos = xsp;
    lookingAhead = true;
    jj_semLA = isRegularForStatement();
    lookingAhead = false;
    if (!jj_semLA || jj_3R_83()) {
    jj_scanpos = xsp;
    if (jj_3R_84()) {
    jj_scanpos = xsp;
    if (jj_3R_85()) {
    jj_scanpos = xsp;
    if (jj_3R_86()) {
    jj_scanpos = xsp;
    if (jj_3R_87()) {
    jj_scanpos = xsp;
    if (jj_3R_88()) {
    jj_scanpos = xsp;
    if (jj_3R_89()) {
    jj_scanpos = xsp;
    if (jj_3R_90()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3_22() {
    if (jj_3R_40()) return true;
    return false;
  }

  final private boolean jj_3R_34() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(81)) {
    jj_scanpos = xsp;
    if (jj_scan_token(120)) {
    jj_scanpos = xsp;
    if (jj_scan_token(121)) {
    jj_scanpos = xsp;
    if (jj_scan_token(127)) {
    jj_scanpos = xsp;
    if (jj_scan_token(118)) {
    jj_scanpos = xsp;
    if (jj_scan_token(119)) {
    jj_scanpos = xsp;
    if (jj_scan_token(122)) {
    jj_scanpos = xsp;
    if (jj_scan_token(126)) {
    jj_scanpos = xsp;
    if (jj_scan_token(124)) {
    jj_scanpos = xsp;
    if (jj_scan_token(128)) {
    jj_scanpos = xsp;
    if (jj_scan_token(129)) {
    jj_scanpos = xsp;
    if (jj_scan_token(130)) {
    jj_scanpos = xsp;
    if (jj_scan_token(131)) {
    jj_scanpos = xsp;
    if (jj_scan_token(132)) {
    jj_scanpos = xsp;
    if (jj_scan_token(133)) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_111() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_29()) return true;
    return false;
  }

  final private boolean jj_3R_160() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_152() {
    if (jj_3R_69()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_17()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_157() {
    Token xsp;
    if (jj_3R_160()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_160()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_97()) return true;
    return false;
  }

  final private boolean jj_3_8() {
    if (jj_3R_33()) return true;
    if (jj_3R_34()) return true;
    return false;
  }

  final private boolean jj_3_20() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_151() {
    if (jj_3R_150()) return true;
    return false;
  }

  final private boolean jj_3_19() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_107() {
    if (jj_3R_33()) return true;
    if (jj_3R_34()) return true;
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3_21() {
    Token xsp;
    if (jj_3_19()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_19()) { jj_scanpos = xsp; break; }
    }
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_20()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_150() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_21()) {
    jj_scanpos = xsp;
    if (jj_3R_157()) return true;
    }
    return false;
  }

  final private boolean jj_3R_71() {
    if (jj_3R_108()) return true;
    return false;
  }

  final private boolean jj_3R_39() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_70()) {
    jj_scanpos = xsp;
    if (jj_3R_71()) return true;
    }
    return false;
  }

  final private boolean jj_3R_70() {
    if (jj_3R_107()) return true;
    return false;
  }

  final private boolean jj_3R_145() {
    if (jj_scan_token(NEW)) return true;
    if (jj_3R_29()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_151()) {
    jj_scanpos = xsp;
    if (jj_3R_152()) return true;
    }
    return false;
  }

  final private boolean jj_3_18() {
    if (jj_scan_token(NEW)) return true;
    if (jj_3R_36()) return true;
    if (jj_3R_150()) return true;
    return false;
  }

  final private boolean jj_3R_130() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_18()) {
    jj_scanpos = xsp;
    if (jj_3R_145()) return true;
    }
    return false;
  }

  final private boolean jj_3R_147() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_76() {
    if (jj_3R_29()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_111()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_134() {
    if (jj_3R_39()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_147()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_106() {
    if (jj_3R_134()) return true;
    return false;
  }

  final private boolean jj_3_7() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_69() {
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_106()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_29() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_7()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_68() {
    if (jj_scan_token(DOUBLE)) return true;
    return false;
  }

  final private boolean jj_3R_67() {
    if (jj_scan_token(FLOAT)) return true;
    return false;
  }

  final private boolean jj_3R_155() {
    if (jj_scan_token(FALSE)) return true;
    return false;
  }

  final private boolean jj_3R_66() {
    if (jj_scan_token(LONG)) return true;
    return false;
  }

  final private boolean jj_3R_65() {
    if (jj_scan_token(INT)) return true;
    return false;
  }

  final private boolean jj_3R_154() {
    if (jj_scan_token(TRUE)) return true;
    return false;
  }

  final private boolean jj_3R_149() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_154()) {
    jj_scanpos = xsp;
    if (jj_3R_155()) return true;
    }
    return false;
  }

  final private boolean jj_3R_64() {
    if (jj_scan_token(SHORT)) return true;
    return false;
  }

  final private boolean jj_3R_56() {
    if (jj_3R_29()) return true;
    return false;
  }

  final private boolean jj_3R_63() {
    if (jj_scan_token(BYTE)) return true;
    return false;
  }

  final private boolean jj_3R_62() {
    if (jj_scan_token(CHAR)) return true;
    return false;
  }

  final private boolean jj_3R_61() {
    if (jj_scan_token(BOOLEAN)) return true;
    return false;
  }

  final private boolean jj_3R_36() {
    Token xsp;
    xsp = jj_scanpos;
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
    if (jj_3R_67()) {
    jj_scanpos = xsp;
    if (jj_3R_68()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_144() {
    if (jj_scan_token(57)) return true;
    return false;
  }

  final private boolean jj_3R_74() {
    if (jj_3R_32()) return true;
    return false;
  }

  final private boolean jj_3R_42() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_73()) {
    jj_scanpos = xsp;
    if (jj_3R_74()) return true;
    }
    return false;
  }

  final private boolean jj_3R_73() {
    if (jj_scan_token(VOID)) return true;
    return false;
  }

  final private boolean jj_3R_143() {
    if (jj_scan_token(41)) return true;
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_142() {
    if (jj_3R_149()) return true;
    return false;
  }

  final private boolean jj_3R_55() {
    if (jj_3R_36()) return true;
    return false;
  }

  final private boolean jj_3R_110() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_109()) return true;
    return false;
  }

  final private boolean jj_3R_141() {
    if (jj_scan_token(STRING_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_32() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_55()) {
    jj_scanpos = xsp;
    if (jj_3R_56()) return true;
    }
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_6()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_140() {
    if (jj_scan_token(CHARACTER_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_190() {
    if (jj_scan_token(FINALLY)) return true;
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_31()) return true;
    return false;
  }

  final private boolean jj_3R_189() {
    if (jj_scan_token(CATCH)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_109()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3R_136() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3_5() {
    if (jj_3R_32()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_75() {
    if (jj_3R_109()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_110()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_109() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_5()) {
    jj_scanpos = xsp;
    if (jj_3R_136()) return true;
    }
    return false;
  }

  final private boolean jj_3R_124() {
    if (jj_scan_token(TRY)) return true;
    if (jj_3R_38()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_189()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_3R_190()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_43() {
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_75()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_163() {
    if (jj_3R_31()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_4()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_139() {
    if (jj_scan_token(FLOATING_POINT_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_97() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_163()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(79)) jj_scanpos = xsp;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_30() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(STAR)) return true;
    return false;
  }

  final private boolean jj_3R_123() {
    if (jj_scan_token(THROW)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_180() {
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_3R_31()) return true;
    return false;
  }

  final private boolean jj_3R_54() {
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_188() {
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_53() {
    if (jj_3R_97()) return true;
    return false;
  }

  final private boolean jj_3R_31() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_53()) {
    jj_scanpos = xsp;
    if (jj_3R_54()) return true;
    }
    return false;
  }

  final private boolean jj_3R_122() {
    if (jj_scan_token(SYNCHRONIZED)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3R_177() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_176()) return true;
    return false;
  }

  final private boolean jj_3R_210() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_112()) return true;
    return false;
  }

  final private boolean jj_3R_121() {
    if (jj_scan_token(RETURN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_188()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_129() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_138()) {
    jj_scanpos = xsp;
    if (jj_3R_139()) {
    jj_scanpos = xsp;
    if (jj_3R_140()) {
    jj_scanpos = xsp;
    if (jj_3R_141()) {
    jj_scanpos = xsp;
    if (jj_3R_142()) {
    jj_scanpos = xsp;
    if (jj_3R_143()) {
    jj_scanpos = xsp;
    if (jj_3R_144()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_138() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_146() {
    if (jj_3R_69()) return true;
    return false;
  }

  final private boolean jj_3R_176() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_180()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_105() {
    if (jj_3R_129()) return true;
    return false;
  }

  final private boolean jj_3R_120() {
    if (jj_scan_token(CONTINUE)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(69)) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_119() {
    if (jj_scan_token(BREAK)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(69)) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_195() {
    if (jj_3R_205()) return true;
    return false;
  }

  final private boolean jj_3R_128() {
    if (jj_scan_token(IMPORT)) return true;
    if (jj_scan_token(STAR)) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_133() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_205() {
    if (jj_3R_112()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_210()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_132() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_146()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3_3() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(48)) jj_scanpos = xsp;
    if (jj_scan_token(IMPORT)) return true;
    if (jj_3R_29()) return true;
    xsp = jj_scanpos;
    if (jj_3R_30()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_94() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_3()) {
    jj_scanpos = xsp;
    if (jj_3R_128()) return true;
    }
    return false;
  }

  final private boolean jj_3R_93() {
    if (jj_3R_41()) return true;
    if (jj_3R_32()) return true;
    if (jj_3R_176()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_177()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_131() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_95() {
    if (jj_scan_token(PACKAGE)) return true;
    if (jj_3R_29()) return true;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_175() {
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3_16() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(CLASS)) return true;
    return false;
  }

  final private boolean jj_3R_104() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_16()) {
    jj_scanpos = xsp;
    if (jj_3R_131()) {
    jj_scanpos = xsp;
    if (jj_3R_132()) {
    jj_scanpos = xsp;
    if (jj_3R_133()) return true;
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_174() {
    if (jj_scan_token(THROWS)) return true;
    if (jj_3R_76()) return true;
    return false;
  }

  final private boolean jj_3_15() {
    if (jj_3R_32()) return true;
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(CLASS)) return true;
    return false;
  }

  final private boolean jj_3_31() {
    if (jj_3R_41()) return true;
    if (jj_3R_32()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3_14() {
    if (jj_3R_37()) return true;
    return false;
  }

  final private boolean jj_3R_126() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_127() {
    if (jj_3R_42()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_92() {
    if (jj_3R_41()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_126()) {
    jj_scanpos = xsp;
    if (jj_3R_127()) return true;
    }
    if (jj_3R_43()) return true;
    xsp = jj_scanpos;
    if (jj_3R_174()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_175()) {
    jj_scanpos = xsp;
    if (jj_scan_token(78)) return true;
    }
    return false;
  }

  final private boolean jj_3R_204() {
    if (jj_3R_205()) return true;
    return false;
  }

  final private boolean jj_3R_103() {
    if (jj_3R_29()) return true;
    return false;
  }

  final private boolean jj_3R_203() {
    if (jj_3R_93()) return true;
    return false;
  }

  final private boolean jj_3R_194() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_203()) {
    jj_scanpos = xsp;
    if (jj_3R_204()) return true;
    }
    return false;
  }

  final private boolean jj_3R_102() {
    if (jj_3R_32()) return true;
    return false;
  }

  final private boolean jj_3R_58() {
    if (jj_3R_104()) return true;
    return false;
  }

  final private boolean jj_3R_125() {
    if (jj_scan_token(INTERFACE)) return true;
    return false;
  }

  final private boolean jj_3R_101() {
    if (jj_3R_37()) return true;
    return false;
  }

  final private boolean jj_3R_100() {
    if (jj_3R_130()) return true;
    return false;
  }

  final private boolean jj_3R_99() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_137() {
    if (jj_scan_token(FOR)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_32()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_184() {
    if (jj_scan_token(ELSE)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_173() {
    if (jj_scan_token(IMPLEMENTS)) return true;
    if (jj_3R_76()) return true;
    return false;
  }

  final private boolean jj_3R_57() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_98()) {
    jj_scanpos = xsp;
    if (jj_3R_99()) {
    jj_scanpos = xsp;
    if (jj_3R_100()) {
    jj_scanpos = xsp;
    if (jj_3R_101()) {
    jj_scanpos = xsp;
    if (jj_3R_102()) {
    jj_scanpos = xsp;
    if (jj_3R_103()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_98() {
    if (jj_3R_129()) return true;
    return false;
  }

  final private boolean jj_3R_172() {
    if (jj_scan_token(EXTENDS)) return true;
    if (jj_3R_29()) return true;
    return false;
  }

  final private boolean jj_3_30() {
    if (jj_scan_token(FOR)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_118() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_30()) {
    jj_scanpos = xsp;
    if (jj_3R_137()) return true;
    }
    return false;
  }

  final private boolean jj_3R_37() {
    if (jj_3R_29()) return true;
    if (jj_3R_69()) return true;
    return false;
  }

  final private boolean jj_3R_185() {
    if (jj_3R_194()) return true;
    return false;
  }

  final private boolean jj_3R_91() {
    if (jj_3R_41()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(13)) {
    jj_scanpos = xsp;
    if (jj_3R_125()) return true;
    }
    if (jj_scan_token(IDENTIFIER)) return true;
    xsp = jj_scanpos;
    if (jj_3R_172()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_173()) jj_scanpos = xsp;
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3_13() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_36()) return true;
    return false;
  }

  final private boolean jj_3R_187() {
    if (jj_3R_195()) return true;
    return false;
  }

  final private boolean jj_3R_186() {
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_33() {
    if (jj_3R_57()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_58()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_217() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_32()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_208()) return true;
    return false;
  }

  final private boolean jj_3R_216() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_32()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_191()) return true;
    return false;
  }

  final private boolean jj_3R_117() {
    if (jj_scan_token(FOR)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_185()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    xsp = jj_scanpos;
    if (jj_3R_186()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    xsp = jj_scanpos;
    if (jj_3R_187()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_214() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_216()) {
    jj_scanpos = xsp;
    if (jj_3R_217()) return true;
    }
    return false;
  }

  final private boolean jj_3_12() {
    if (jj_3R_33()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(100)) {
    jj_scanpos = xsp;
    if (jj_scan_token(101)) return true;
    }
    return false;
  }

  final private boolean jj_3R_219() {
    if (jj_3R_33()) return true;
    return false;
  }

  final private boolean jj_3R_116() {
    if (jj_scan_token(DO)) return true;
    if (jj_3R_45()) return true;
    if (jj_scan_token(WHILE)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3_11() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_29()) return true;
    if (jj_scan_token(LBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_218() {
    if (jj_3R_33()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(100)) {
    jj_scanpos = xsp;
    if (jj_scan_token(101)) return true;
    }
    return false;
  }

  final private boolean jj_3R_215() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_218()) {
    jj_scanpos = xsp;
    if (jj_3R_219()) return true;
    }
    return false;
  }

  final private boolean jj_3R_72() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(43)) {
    jj_scanpos = xsp;
    if (jj_scan_token(44)) {
    jj_scanpos = xsp;
    if (jj_scan_token(45)) {
    jj_scanpos = xsp;
    if (jj_scan_token(51)) {
    jj_scanpos = xsp;
    if (jj_scan_token(27)) {
    jj_scanpos = xsp;
    if (jj_scan_token(39)) {
    jj_scanpos = xsp;
    if (jj_scan_token(52)) {
    jj_scanpos = xsp;
    if (jj_scan_token(58)) {
    jj_scanpos = xsp;
    if (jj_scan_token(10)) {
    jj_scanpos = xsp;
    if (jj_scan_token(48)) {
    jj_scanpos = xsp;
    if (jj_scan_token(49)) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_115() {
    if (jj_scan_token(WHILE)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_60() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_29()) return true;
    if (jj_scan_token(RPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(87)) {
    jj_scanpos = xsp;
    if (jj_scan_token(86)) {
    jj_scanpos = xsp;
    if (jj_scan_token(72)) {
    jj_scanpos = xsp;
    if (jj_scan_token(69)) {
    jj_scanpos = xsp;
    if (jj_scan_token(40)) {
    jj_scanpos = xsp;
    if (jj_3R_105()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_59() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_29()) return true;
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3_9() {
    if (jj_3R_35()) return true;
    return false;
  }

  final private boolean jj_3_29() {
    if (jj_3R_28()) return true;
    return false;
  }

  final private boolean jj_3R_114() {
    if (jj_scan_token(IF)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_45()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_184()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_41() {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_72()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_35() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_10()) {
    jj_scanpos = xsp;
    if (jj_3R_59()) {
    jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3_10() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_36()) return true;
    return false;
  }

  final private boolean jj_3R_213() {
    if (jj_3R_215()) return true;
    return false;
  }

  final private boolean jj_3R_212() {
    if (jj_3R_214()) return true;
    return false;
  }

  final private boolean jj_3R_202() {
    if (jj_scan_token(_DEFAULT)) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_211() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(87)) {
    jj_scanpos = xsp;
    if (jj_scan_token(86)) return true;
    }
    if (jj_3R_191()) return true;
    return false;
  }

  final private boolean jj_3R_208() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_211()) {
    jj_scanpos = xsp;
    if (jj_3R_212()) {
    jj_scanpos = xsp;
    if (jj_3R_213()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_201() {
    if (jj_scan_token(CASE)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_193() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_201()) {
    jj_scanpos = xsp;
    if (jj_3R_202()) return true;
    }
    return false;
  }

  final private boolean jj_3R_183() {
    if (jj_3R_193()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_29()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_207() {
    if (jj_scan_token(DECR)) return true;
    if (jj_3R_33()) return true;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_3R_28()) return true;
    return false;
  }

  final private boolean jj_3R_113() {
    if (jj_scan_token(SWITCH)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_39()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_183()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_209() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(104)) {
    jj_scanpos = xsp;
    if (jj_scan_token(105)) {
    jj_scanpos = xsp;
    if (jj_scan_token(111)) return true;
    }
    }
    if (jj_3R_191()) return true;
    return false;
  }

  final private boolean jj_3R_206() {
    if (jj_scan_token(INCR)) return true;
    if (jj_3R_33()) return true;
    return false;
  }

  final private boolean jj_3R_199() {
    if (jj_3R_208()) return true;
    return false;
  }

  final private boolean jj_3R_198() {
    if (jj_3R_207()) return true;
    return false;
  }

  final private boolean jj_3R_197() {
    if (jj_3R_206()) return true;
    return false;
  }

  final private boolean jj_3R_196() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(102)) {
    jj_scanpos = xsp;
    if (jj_scan_token(103)) return true;
    }
    if (jj_3R_191()) return true;
    return false;
  }

  final private boolean jj_3R_191() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_196()) {
    jj_scanpos = xsp;
    if (jj_3R_197()) {
    jj_scanpos = xsp;
    if (jj_3R_198()) {
    jj_scanpos = xsp;
    if (jj_3R_199()) return true;
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_44() {
    if (jj_scan_token(THROWS)) return true;
    if (jj_3R_76()) return true;
    return false;
  }

  final private boolean jj_3R_112() {
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_181() {
    if (jj_3R_191()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_209()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_200() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(102)) {
    jj_scanpos = xsp;
    if (jj_scan_token(103)) return true;
    }
    if (jj_3R_181()) return true;
    return false;
  }

  final private boolean jj_3R_178() {
    if (jj_3R_181()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_200()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_96() {
    if (jj_scan_token(FORMAL_COMMENT)) return true;
    return false;
  }

  final private boolean jj_3R_192() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(112)) {
    jj_scanpos = xsp;
    if (jj_scan_token(113)) {
    jj_scanpos = xsp;
    if (jj_scan_token(114)) {
    jj_scanpos = xsp;
    if (jj_scan_token(115)) {
    jj_scanpos = xsp;
    if (jj_scan_token(116)) {
    jj_scanpos = xsp;
    if (jj_scan_token(117)) return true;
    }
    }
    }
    }
    }
    if (jj_3R_178()) return true;
    return false;
  }

  final private boolean jj_3R_171() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(90)) {
    jj_scanpos = xsp;
    if (jj_scan_token(95)) return true;
    }
    if (jj_3R_166()) return true;
    return false;
  }

  final private boolean jj_3R_170() {
    if (jj_3R_178()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_192()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_52() {
    if (jj_3R_96()) return true;
    return false;
  }

  final private boolean jj_3R_182() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(84)) {
    jj_scanpos = xsp;
    if (jj_scan_token(85)) {
    jj_scanpos = xsp;
    if (jj_scan_token(82)) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) {
    jj_scanpos = xsp;
    if (jj_scan_token(91)) {
    jj_scanpos = xsp;
    if (jj_scan_token(92)) {
    jj_scanpos = xsp;
    if (jj_scan_token(93)) {
    jj_scanpos = xsp;
    if (jj_scan_token(94)) return true;
    }
    }
    }
    }
    }
    }
    }
    if (jj_3R_170()) return true;
    return false;
  }

  final private boolean jj_3_27() {
    if (jj_3R_41()) return true;
    if (jj_3R_32()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_51() {
    if (jj_3R_95()) return true;
    return false;
  }

  final private boolean jj_3R_168() {
    if (jj_3R_170()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_182()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_50() {
    if (jj_3R_94()) return true;
    return false;
  }

  final private boolean jj_3_26() {
    if (jj_3R_41()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_3R_43()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_44()) jj_scanpos = xsp;
    if (jj_scan_token(LBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_179() {
    if (jj_scan_token(INSTANCEOF)) return true;
    if (jj_3R_32()) return true;
    return false;
  }

  final private boolean jj_3_28() {
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_166() {
    if (jj_3R_168()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_179()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3_25() {
    if (jj_3R_41()) return true;
    if (jj_3R_42()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_49() {
    if (jj_3R_93()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3_24() {
    if (jj_3R_41()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(13)) {
    jj_scanpos = xsp;
    if (jj_scan_token(37)) return true;
    }
    return false;
  }

  final private boolean jj_3R_167() {
    if (jj_scan_token(XOR)) return true;
    if (jj_3R_161()) return true;
    return false;
  }

  final private boolean jj_3R_48() {
    if (jj_3R_92()) return true;
    return false;
  }

  final private boolean jj_3R_164() {
    if (jj_3R_166()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_171()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_47() {
    if (jj_3R_92()) return true;
    return false;
  }

  final private boolean jj_3R_169() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(106)) {
    jj_scanpos = xsp;
    if (jj_scan_token(107)) return true;
    }
    if (jj_3R_164()) return true;
    return false;
  }

  public ParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;

  public Parser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
  }

  public Parser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
  }

  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      return token;
    }
    token = oldToken;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
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
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
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

  public ParseException generateParseException() {
    Token errortok = token.next;
    int line = errortok.beginLine, column = errortok.beginColumn;
    String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
    return new ParseException("Parse error at line " + line + ", column " + column + ".  Encountered: " + mess);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
