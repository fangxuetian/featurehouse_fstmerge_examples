
package bsh;
import java.io.*;
import java.util.Vector;

public class ParserTokenManager implements ParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2)
{
   switch (pos)
   {
      case 0:
         if ((active1 & 0x200020000000000L) != 0L)
            return 56;
         if ((active0 & 0x3eL) != 0L)
            return 0;
         if ((active1 & 0x10000L) != 0L)
            return 11;
         if ((active0 & 0xffffffffffffc00L) != 0L)
         {
            jjmatchedKind = 69;
            return 35;
         }
         return -1;
      case 1:
         if ((active0 & 0x100600000L) != 0L)
            return 35;
         if ((active0 & 0xffffffeff9ffc00L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 69;
               jjmatchedPos = 1;
            }
            return 35;
         }
         return -1;
      case 2:
         if ((active0 & 0xefffecebfdffc00L) != 0L)
         {
            if (jjmatchedPos != 2)
            {
               jjmatchedKind = 69;
               jjmatchedPos = 2;
            }
            return 35;
         }
         if ((active0 & 0x100013040000000L) != 0L)
            return 35;
         return -1;
      case 3:
         if ((active0 & 0xc7ffcae3e5d3c00L) != 0L)
         {
            if (jjmatchedPos != 3)
            {
               jjmatchedKind = 69;
               jjmatchedPos = 3;
            }
            return 35;
         }
         if ((active0 & 0x28002408182c000L) != 0L)
            return 35;
         return -1;
      case 4:
         if ((active0 & 0x86080003c053000L) != 0L)
            return 35;
         if ((active0 & 0x41f7cae02580c00L) != 0L)
         {
            if (jjmatchedPos != 4)
            {
               jjmatchedKind = 69;
               jjmatchedPos = 4;
            }
            return 35;
         }
         return -1;
      case 5:
         if ((active0 & 0x41a1c2a12180c00L) != 0L)
         {
            jjmatchedKind = 69;
            jjmatchedPos = 5;
            return 35;
         }
         if ((active0 & 0x45608400400000L) != 0L)
            return 35;
         return -1;
      case 6:
         if ((active0 & 0x41a102a00080400L) != 0L)
         {
            jjmatchedKind = 69;
            jjmatchedPos = 6;
            return 35;
         }
         if ((active0 & 0xc0012100800L) != 0L)
            return 35;
         return -1;
      case 7:
         if ((active0 & 0x402000000080400L) != 0L)
            return 35;
         if ((active0 & 0x18102a00000000L) != 0L)
         {
            jjmatchedKind = 69;
            jjmatchedPos = 7;
            return 35;
         }
         return -1;
      case 8:
         if ((active0 & 0x8000a00000000L) != 0L)
         {
            jjmatchedKind = 69;
            jjmatchedPos = 8;
            return 35;
         }
         if ((active0 & 0x10102000000000L) != 0L)
            return 35;
         return -1;
      case 9:
         if ((active0 & 0x8000000000000L) != 0L)
         {
            jjmatchedKind = 69;
            jjmatchedPos = 9;
            return 35;
         }
         if ((active0 & 0xa00000000L) != 0L)
            return 35;
         return -1;
      case 10:
         if ((active0 & 0x8000000000000L) != 0L)
         {
            if (jjmatchedPos != 10)
            {
               jjmatchedKind = 69;
               jjmatchedPos = 10;
            }
            return 35;
         }
         return -1;
      case 11:
         if ((active0 & 0x8000000000000L) != 0L)
            return 35;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0, long active1, long active2)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         return jjStartNfaWithStates_0(0, 2, 0);
      case 10:
         return jjStartNfaWithStates_0(0, 5, 0);
      case 12:
         return jjStartNfaWithStates_0(0, 4, 0);
      case 13:
         return jjStartNfaWithStates_0(0, 3, 0);
      case 32:
         return jjStartNfaWithStates_0(0, 1, 0);
      case 33:
         jjmatchedKind = 86;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x80000000L, 0x0L);
      case 37:
         jjmatchedKind = 111;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x8000000000000000L, 0x0L);
      case 38:
         jjmatchedKind = 106;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x400000400000000L, 0x0L);
      case 40:
         return jjStopAtPos(0, 72);
      case 41:
         return jjStopAtPos(0, 73);
      case 42:
         jjmatchedKind = 104;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x100000000000000L, 0x0L);
      case 43:
         jjmatchedKind = 102;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x40001000000000L, 0x0L);
      case 44:
         return jjStopAtPos(0, 79);
      case 45:
         jjmatchedKind = 103;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x80002000000000L, 0x0L);
      case 46:
         return jjStartNfaWithStates_0(0, 80, 11);
      case 47:
         jjmatchedKind = 105;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x200000000000000L, 0x0L);
      case 58:
         return jjStopAtPos(0, 89);
      case 59:
         return jjStopAtPos(0, 78);
      case 60:
         jjmatchedKind = 84;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x1000008000000L, 0x1L);
      case 61:
         jjmatchedKind = 81;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x4000000L, 0x0L);
      case 62:
         jjmatchedKind = 82;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x14000020000000L, 0x14L);
      case 63:
         return jjStopAtPos(0, 88);
      case 64:
         return jjMoveStringLiteralDfa1_0(0x0L, 0x282a280a50280000L, 0x2aL);
      case 91:
         return jjStopAtPos(0, 76);
      case 93:
         return jjStopAtPos(0, 77);
      case 94:
         jjmatchedKind = 110;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x4000000000000000L, 0x0L);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x400L, 0x0L, 0x0L);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x5800L, 0x0L, 0x0L);
      case 99:
         return jjMoveStringLiteralDfa1_0(0xfa000L, 0x0L, 0x0L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x700000L, 0x0L, 0x0L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x3800000L, 0x0L, 0x0L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x7c000000L, 0x0L, 0x0L);
      case 103:
         return jjMoveStringLiteralDfa1_0(0x80000000L, 0x0L, 0x0L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x3f00000000L, 0x0L, 0x0L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x4000000000L, 0x0L, 0x0L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x38000000000L, 0x0L, 0x0L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x3c0000000000L, 0x0L, 0x0L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x400000000000L, 0x0L, 0x0L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0xf800000000000L, 0x0L, 0x0L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x1f0000000000000L, 0x0L, 0x0L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x600000000000000L, 0x0L, 0x0L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x800000000000000L, 0x0L, 0x0L);
      case 123:
         return jjStopAtPos(0, 74);
      case 124:
         jjmatchedKind = 108;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x1000000100000000L, 0x0L);
      case 125:
         return jjStopAtPos(0, 75);
      case 126:
         return jjStopAtPos(0, 87);
      default :
         return jjMoveNfa_0(6, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0, active1, active2);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active1 & 0x400000000L) != 0L)
            return jjStopAtPos(1, 98);
         break;
      case 43:
         if ((active1 & 0x1000000000L) != 0L)
            return jjStopAtPos(1, 100);
         break;
      case 45:
         if ((active1 & 0x2000000000L) != 0L)
            return jjStopAtPos(1, 101);
         break;
      case 60:
         if ((active1 & 0x1000000000000L) != 0L)
         {
            jjmatchedKind = 112;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 0x1L);
      case 61:
         if ((active1 & 0x4000000L) != 0L)
            return jjStopAtPos(1, 90);
         else if ((active1 & 0x8000000L) != 0L)
            return jjStopAtPos(1, 91);
         else if ((active1 & 0x20000000L) != 0L)
            return jjStopAtPos(1, 93);
         else if ((active1 & 0x80000000L) != 0L)
            return jjStopAtPos(1, 95);
         else if ((active1 & 0x40000000000000L) != 0L)
            return jjStopAtPos(1, 118);
         else if ((active1 & 0x80000000000000L) != 0L)
            return jjStopAtPos(1, 119);
         else if ((active1 & 0x100000000000000L) != 0L)
            return jjStopAtPos(1, 120);
         else if ((active1 & 0x200000000000000L) != 0L)
            return jjStopAtPos(1, 121);
         else if ((active1 & 0x400000000000000L) != 0L)
            return jjStopAtPos(1, 122);
         else if ((active1 & 0x1000000000000000L) != 0L)
            return jjStopAtPos(1, 124);
         else if ((active1 & 0x4000000000000000L) != 0L)
            return jjStopAtPos(1, 126);
         else if ((active1 & 0x8000000000000000L) != 0L)
            return jjStopAtPos(1, 127);
         break;
      case 62:
         if ((active1 & 0x4000000000000L) != 0L)
         {
            jjmatchedKind = 114;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x10000000000000L, active2, 0x14L);
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x48004018000L, active1, 0x800000800000000L, active2, 0L);
      case 98:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L, active1, 0x280000000000L, active2, 0L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x410000100000L, active1, 0L, active2, 0L);
      case 102:
         if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(1, 32, 35);
         break;
      case 103:
         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x40080000L, active2, 0L);
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x860800000020000L, active1, 0L, active2, 0L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x18000000L, active1, 0L, active2, 0L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x20802000L, active1, 0x2000010200000L, active2, 0x2L);
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x600000000L, active1, 0L, active2, 0L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x3801000000L, active1, 0L, active2, 0L);
      case 111:
         if ((active0 & 0x200000L) != 0L)
         {
            jjmatchedKind = 21;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x6000040c04c0800L, active1, 0x2000000200000000L, active2, 0L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x190180000001000L, active1, 0x28000000000000L, active2, 0x28L);
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x3000000000000L, active1, 0L, active2, 0L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x220000000000L, active1, 0L, active2, 0L);
      case 119:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000000000L, active1, 0L, active2, 0L);
      case 120:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000000L, active1, 0L, active2, 0L);
      case 121:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000000004000L, active1, 0L, active2, 0L);
      case 124:
         if ((active1 & 0x100000000L) != 0L)
            return jjStopAtPos(1, 96);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(0, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0, active1, active2);
      return 2;
   }
   switch(curChar)
   {
      case 61:
         if ((active2 & 0x1L) != 0L)
            return jjStopAtPos(2, 128);
         else if ((active2 & 0x4L) != 0L)
            return jjStopAtPos(2, 130);
         break;
      case 62:
         if ((active1 & 0x10000000000000L) != 0L)
         {
            jjmatchedKind = 116;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 0x10L);
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x11000000022000L, active1, 0L, active2, 0L);
      case 98:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000000000L, active1, 0L, active2, 0L);
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x40000000000L, active1, 0L, active2, 0L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L, active1, 0x2000000000000L, active2, 0x2L);
      case 102:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000L, active1, 0L, active2, 0L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0xa04080000000000L, active1, 0x28280000000000L, active2, 0x28L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x400020004000000L, active1, 0L, active2, 0L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x80040180c0000L, active1, 0x800000800000000L, active2, 0L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x900020000800L, active1, 0L, active2, 0L);
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x600000000L, active1, 0L, active2, 0L);
      case 114:
         if ((active0 & 0x40000000L) != 0L)
            return jjStartNfaWithStates_0(2, 30, 35);
         else if ((active1 & 0x200000000L) != 0L)
         {
            jjmatchedKind = 97;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x62000000000000L, active1, 0x2000000000000000L, active2, 0L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x800808400L, active1, 0L, active2, 0L);
      case 116:
         if ((active0 & 0x1000000000L) != 0L)
         {
            jjmatchedKind = 36;
            jjmatchedPos = 2;
         }
         else if ((active1 & 0x80000L) != 0L)
         {
            jjmatchedKind = 83;
            jjmatchedPos = 2;
         }
         else if ((active1 & 0x200000L) != 0L)
         {
            jjmatchedKind = 85;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x40a082014000L, active1, 0x50000000L, active2, 0L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000001400000L, active1, 0L, active2, 0L);
      case 119:
         if ((active0 & 0x10000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 40, 35);
         break;
      case 121:
         if ((active0 & 0x100000000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 56, 35);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(1, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0, active1, active2);
      return 3;
   }
   switch(curChar)
   {
      case 61:
         if ((active2 & 0x10L) != 0L)
            return jjStopAtPos(3, 132);
         break;
      case 95:
         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x2000000000000000L, active2, 0L);
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000038101000L, active1, 0L, active2, 0L);
      case 98:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000L, active1, 0L, active2, 0L);
      case 99:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000000010000L, active1, 0L, active2, 0L);
      case 100:
         if ((active0 & 0x200000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 57, 35);
         else if ((active1 & 0x800000000L) != 0L)
         {
            jjmatchedKind = 99;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x800000000000000L, active2, 0L);
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 35);
         else if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(3, 15, 35);
         else if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(3, 23, 35);
         else if ((active0 & 0x80000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 55, 35);
         return jjMoveStringLiteralDfa4_0(active0, 0x2002000000L, active1, 0x50000000L, active2, 0L);
      case 102:
         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x2000000000000L, active2, 0x2L);
      case 103:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 38, 35);
         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x28000000000000L, active2, 0x28L);
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x2008000000000L, active1, 0L, active2, 0L);
      case 107:
         return jjMoveStringLiteralDfa4_0(active0, 0x40000000000L, active1, 0L, active2, 0L);
      case 108:
         if ((active0 & 0x20000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 41, 35);
         return jjMoveStringLiteralDfa4_0(active0, 0x800200200000800L, active1, 0L, active2, 0L);
      case 109:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(3, 24, 35);
         break;
      case 110:
         return jjMoveStringLiteralDfa4_0(active0, 0x10000000000000L, active1, 0L, active2, 0L);
      case 111:
         if ((active0 & 0x80000000L) != 0L)
            return jjStartNfaWithStates_0(3, 31, 35);
         return jjMoveStringLiteralDfa4_0(active0, 0x60000400000000L, active1, 0L, active2, 0L);
      case 114:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(3, 17, 35);
         return jjMoveStringLiteralDfa4_0(active0, 0x800000000000L, active1, 0L, active2, 0L);
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x4042000L, active1, 0L, active2, 0L);
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x5100800080400L, active1, 0x280000000000L, active2, 0L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000000000L, active1, 0L, active2, 0L);
      case 118:
         return jjMoveStringLiteralDfa4_0(active0, 0x80000000000L, active1, 0L, active2, 0L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(2, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0, active1, active2);
      return 4;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x800000000000000L, active2, 0L);
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0xc0800000000L, active1, 0x2000000000000000L, active2, 0L);
      case 99:
         return jjMoveStringLiteralDfa5_0(active0, 0x6000000000000L, active1, 0L, active2, 0L);
      case 101:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(4, 26, 35);
         else if ((active0 & 0x800000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 59, 35);
         return jjMoveStringLiteralDfa5_0(active0, 0x100200000800L, active1, 0L, active2, 0L);
      case 104:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(4, 16, 35);
         return jjMoveStringLiteralDfa5_0(active0, 0x8000000000000L, active1, 0x28000000000000L, active2, 0x28L);
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x1200000080000L, active1, 0L, active2, 0L);
      case 107:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(4, 12, 35);
         break;
      case 108:
         if ((active0 & 0x8000000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_0(active0, 0x10400000L, active1, 0L, active2, 0L);
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x2000000L, active1, 0L, active2, 0L);
      case 113:
         if ((active1 & 0x10000000L) != 0L)
            return jjStopAtPos(4, 92);
         else if ((active1 & 0x40000000L) != 0L)
            return jjStopAtPos(4, 94);
         break;
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x402400000400L, active1, 0L, active2, 0L);
      case 115:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(4, 13, 35);
         return jjMoveStringLiteralDfa5_0(active0, 0x10000000000000L, active1, 0L, active2, 0L);
      case 116:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(4, 18, 35);
         else if ((active0 & 0x20000000L) != 0L)
            return jjStartNfaWithStates_0(4, 29, 35);
         else if ((active0 & 0x800000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 47, 35);
         return jjMoveStringLiteralDfa5_0(active0, 0x400000000000000L, active1, 0x2000000000000L, active2, 0x2L);
      case 117:
         return jjMoveStringLiteralDfa5_0(active0, 0x100000L, active1, 0L, active2, 0L);
      case 118:
         return jjMoveStringLiteralDfa5_0(active0, 0x8000000000L, active1, 0L, active2, 0L);
      case 119:
         if ((active0 & 0x20000000000000L) != 0L)
         {
            jjmatchedKind = 53;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_0(active0, 0x40000000000000L, active1, 0x280000000000L, active2, 0L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(3, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0, active1, active2);
      return 5;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x2000000000000L, active2, 0x2L);
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0xc00L, active1, 0x800000000000000L, active2, 0L);
      case 99:
         if ((active0 & 0x200000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 45, 35);
         else if ((active0 & 0x1000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 48, 35);
         return jjMoveStringLiteralDfa6_0(active0, 0x100000000000L, active1, 0L, active2, 0L);
      case 100:
         return jjMoveStringLiteralDfa6_0(active0, 0x2000000L, active1, 0L, active2, 0L);
      case 101:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(5, 22, 35);
         else if ((active0 & 0x8000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 39, 35);
         break;
      case 102:
         return jjMoveStringLiteralDfa6_0(active0, 0x2000000000L, active1, 0L, active2, 0L);
      case 103:
         return jjMoveStringLiteralDfa6_0(active0, 0x40000000000L, active1, 0L, active2, 0L);
      case 104:
         if ((active0 & 0x4000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 50, 35);
         break;
      case 105:
         return jjMoveStringLiteralDfa6_0(active0, 0x410000000000000L, active1, 0x280000000000L, active2, 0L);
      case 108:
         return jjMoveStringLiteralDfa6_0(active0, 0x10100000L, active1, 0L, active2, 0L);
      case 109:
         return jjMoveStringLiteralDfa6_0(active0, 0x200000000L, active1, 0L, active2, 0L);
      case 110:
         if ((active0 & 0x400000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 46, 35);
         return jjMoveStringLiteralDfa6_0(active0, 0x800080000L, active1, 0L, active2, 0L);
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x8000000000000L, active1, 0L, active2, 0L);
      case 115:
         if ((active0 & 0x40000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 54, 35);
         return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x2000000000000000L, active2, 0L);
      case 116:
         if ((active0 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(5, 34, 35);
         return jjMoveStringLiteralDfa6_0(active0, 0x2080000000000L, active1, 0x28000000000000L, active2, 0x28L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(4, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0, active1, active2);
      return 6;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x28000000000000L, active2, 0x28L);
      case 97:
         return jjMoveStringLiteralDfa7_0(active0, 0x2000000000L, active1, 0L, active2, 0L);
      case 99:
         return jjMoveStringLiteralDfa7_0(active0, 0x800000400L, active1, 0L, active2, 0L);
      case 101:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 42, 35);
         else if ((active0 & 0x80000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 43, 35);
         return jjMoveStringLiteralDfa7_0(active0, 0x10000200000000L, active1, 0L, active2, 0L);
      case 102:
         return jjMoveStringLiteralDfa7_0(active0, 0x2000000000000L, active1, 0L, active2, 0L);
      case 108:
         return jjMoveStringLiteralDfa7_0(active0, 0x400000000000000L, active1, 0L, active2, 0L);
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(6, 11, 35);
         break;
      case 111:
         return jjMoveStringLiteralDfa7_0(active0, 0x8000000000000L, active1, 0L, active2, 0L);
      case 115:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(6, 25, 35);
         return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x2802280000000000L, active2, 0x2L);
      case 116:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(6, 20, 35);
         return jjMoveStringLiteralDfa7_0(active0, 0x100000000000L, active1, 0L, active2, 0L);
      case 117:
         return jjMoveStringLiteralDfa7_0(active0, 0x80000L, active1, 0L, active2, 0L);
      case 121:
         if ((active0 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(6, 28, 35);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(5, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0, active1, active2);
      return 7;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa8_0(active0, 0x2000000000L, active1, 0L, active2, 0L);
      case 101:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(7, 19, 35);
         else if ((active0 & 0x400000000000000L) != 0L)
            return jjStartNfaWithStates_0(7, 58, 35);
         return jjMoveStringLiteralDfa8_0(active0, 0x100800000000L, active1, 0x280000000000L, active2, 0L);
      case 104:
         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x2000000000000L, active2, 0x2L);
      case 105:
         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x2000000000000000L, active2, 0L);
      case 110:
         return jjMoveStringLiteralDfa8_0(active0, 0x18000200000000L, active1, 0L, active2, 0L);
      case 112:
         if ((active0 & 0x2000000000000L) != 0L)
            return jjStartNfaWithStates_0(7, 49, 35);
         break;
      case 115:
         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x808000000000000L, active2, 0x8L);
      case 116:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(7, 10, 35);
         break;
      case 117:
         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x20000000000000L, active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(6, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(6, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0, active1, active2);
      return 8;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0x280000000000L, active2, 0L);
      case 100:
         if ((active0 & 0x100000000000L) != 0L)
            return jjStartNfaWithStates_0(8, 44, 35);
         break;
      case 101:
         if ((active0 & 0x2000000000L) != 0L)
            return jjStartNfaWithStates_0(8, 37, 35);
         break;
      case 103:
         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0x2000000000000000L, active2, 0L);
      case 104:
         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0x8000000000000L, active2, 0x8L);
      case 105:
         return jjMoveStringLiteralDfa9_0(active0, 0x8000000000000L, active1, 0x802000000000000L, active2, 0x2L);
      case 110:
         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0x20000000000000L, active2, 0x20L);
      case 111:
         return jjMoveStringLiteralDfa9_0(active0, 0x800000000L, active1, 0L, active2, 0L);
      case 116:
         if ((active0 & 0x10000000000000L) != 0L)
            return jjStartNfaWithStates_0(8, 52, 35);
         return jjMoveStringLiteralDfa9_0(active0, 0x200000000L, active1, 0L, active2, 0L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(7, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0, active1, active2);
      return 9;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x80000000000L, active2, 0L);
      case 102:
         if ((active0 & 0x800000000L) != 0L)
            return jjStartNfaWithStates_0(9, 35, 35);
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x2000000000000L, active2, 0x2L);
      case 103:
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x800000000000000L, active2, 0L);
      case 105:
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x8000000000000L, active2, 0x8L);
      case 110:
         if ((active1 & 0x2000000000000000L) != 0L)
            return jjStopAtPos(9, 125);
         break;
      case 111:
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x200000000000L, active2, 0L);
      case 115:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(9, 33, 35);
         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 0x20000000000000L, active2, 0x20L);
      case 122:
         return jjMoveStringLiteralDfa10_0(active0, 0x8000000000000L, active1, 0L, active2, 0L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa10_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(8, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0, active1, active2);
      return 10;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa11_0(active0, 0x8000000000000L, active1, 0L, active2, 0L);
      case 102:
         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0x8000000000000L, active2, 0x8L);
      case 105:
         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0x20000000000000L, active2, 0x20L);
      case 110:
         if ((active1 & 0x800000000000000L) != 0L)
            return jjStopAtPos(10, 123);
         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0x80000000000L, active2, 0L);
      case 114:
         if ((active1 & 0x200000000000L) != 0L)
            return jjStopAtPos(10, 109);
         break;
      case 116:
         if ((active1 & 0x2000000000000L) != 0L)
         {
            jjmatchedKind = 113;
            jjmatchedPos = 10;
         }
         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0L, active2, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(9, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa11_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(9, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0, active1, active2);
      return 11;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 0x2L);
      case 100:
         if ((active0 & 0x8000000000000L) != 0L)
            return jjStartNfaWithStates_0(11, 51, 35);
         else if ((active1 & 0x80000000000L) != 0L)
            return jjStopAtPos(11, 107);
         break;
      case 103:
         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0x20000000000000L, active2, 0x20L);
      case 116:
         if ((active1 & 0x8000000000000L) != 0L)
         {
            jjmatchedKind = 115;
            jjmatchedPos = 11;
         }
         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 0x8L);
      default :
         break;
   }
   return jjStartNfa_0(10, active0, active1, active2);
}
private final int jjMoveStringLiteralDfa12_0(long old0, long active0, long old1, long active1, long old2, long active2)
{
   if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(10, old0, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(11, 0L, active1, active2);
      return 12;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 0x8L);
      case 97:
         return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 0x2L);
      case 110:
         return jjMoveStringLiteralDfa13_0(active1, 0x20000000000000L, active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(11, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa13_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(11, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(12, 0L, active1, active2);
      return 13;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 0x8L);
      case 101:
         return jjMoveStringLiteralDfa14_0(active1, 0x20000000000000L, active2, 0x20L);
      case 115:
         return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(12, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa14_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(12, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(13, 0L, active1, active2);
      return 14;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa15_0(active1, 0x20000000000000L, active2, 0x20L);
      case 115:
         return jjMoveStringLiteralDfa15_0(active1, 0L, active2, 0xaL);
      default :
         break;
   }
   return jjStartNfa_0(13, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa15_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(13, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(14, 0L, active1, active2);
      return 15;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa16_0(active1, 0x20000000000000L, active2, 0x20L);
      case 105:
         return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 0x2L);
      case 115:
         return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 0x8L);
      default :
         break;
   }
   return jjStartNfa_0(14, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa16_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(14, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(15, 0L, active1, active2);
      return 16;
   }
   switch(curChar)
   {
      case 103:
         return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 0x2L);
      case 105:
         return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 0x8L);
      case 115:
         return jjMoveStringLiteralDfa17_0(active1, 0x20000000000000L, active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(15, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa17_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(15, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(16, 0L, active1, active2);
      return 17;
   }
   switch(curChar)
   {
      case 103:
         return jjMoveStringLiteralDfa18_0(active1, 0L, active2, 0x8L);
      case 104:
         return jjMoveStringLiteralDfa18_0(active1, 0x20000000000000L, active2, 0x20L);
      case 110:
         if ((active2 & 0x2L) != 0L)
            return jjStopAtPos(17, 129);
         break;
      default :
         break;
   }
   return jjStartNfa_0(16, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa18_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(16, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(17, 0L, active1, active2);
      return 18;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa19_0(active1, 0x20000000000000L, active2, 0x20L);
      case 110:
         if ((active2 & 0x8L) != 0L)
            return jjStopAtPos(18, 131);
         break;
      default :
         break;
   }
   return jjStartNfa_0(17, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa19_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(17, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(18, 0L, active1, active2);
      return 19;
   }
   switch(curChar)
   {
      case 102:
         return jjMoveStringLiteralDfa20_0(active1, 0x20000000000000L, active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(18, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa20_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(18, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(19, 0L, active1, active2);
      return 20;
   }
   switch(curChar)
   {
      case 116:
         if ((active1 & 0x20000000000000L) != 0L)
         {
            jjmatchedKind = 117;
            jjmatchedPos = 20;
         }
         return jjMoveStringLiteralDfa21_0(active1, 0L, active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(19, 0L, active1, active2);
}
private final int jjMoveStringLiteralDfa21_0(long old1, long active1, long old2, long active2)
{
   if (((active1 &= old1) | (active2 &= old2)) == 0L)
      return jjStartNfa_0(19, 0L, old1, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(20, 0L, 0L, active2);
      return 21;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa22_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(20, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa22_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(20, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(21, 0L, 0L, active2);
      return 22;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa23_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(21, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa23_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(21, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(22, 0L, 0L, active2);
      return 23;
   }
   switch(curChar)
   {
      case 115:
         return jjMoveStringLiteralDfa24_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(22, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa24_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(22, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(23, 0L, 0L, active2);
      return 24;
   }
   switch(curChar)
   {
      case 115:
         return jjMoveStringLiteralDfa25_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(23, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa25_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(23, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(24, 0L, 0L, active2);
      return 25;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa26_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(24, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa26_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(24, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(25, 0L, 0L, active2);
      return 26;
   }
   switch(curChar)
   {
      case 103:
         return jjMoveStringLiteralDfa27_0(active2, 0x20L);
      default :
         break;
   }
   return jjStartNfa_0(25, 0L, 0L, active2);
}
private final int jjMoveStringLiteralDfa27_0(long old2, long active2)
{
   if (((active2 &= old2)) == 0L)
      return jjStartNfa_0(25, 0L, 0L, old2); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(26, 0L, 0L, active2);
      return 27;
   }
   switch(curChar)
   {
      case 110:
         if ((active2 & 0x20L) != 0L)
            return jjStopAtPos(27, 133);
         break;
      default :
         break;
   }
   return jjStartNfa_0(26, 0L, 0L, active2);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec1 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec3 = {
   0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L
};
static final long[] jjbitVec4 = {
   0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL
};
static final long[] jjbitVec5 = {
   0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec6 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L
};
static final long[] jjbitVec7 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L
};
static final long[] jjbitVec8 = {
   0x3fffffffffffL, 0x0L, 0x0L, 0x0L
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 74;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 6:
                  if ((0x1ffffffffL & l) != 0L)
                  {
                     if (kind > 6)
                        kind = 6;
                     jjCheckNAdd(0);
                  }
                  else if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(0, 6);
                  else if (curChar == 47)
                     jjAddStates(7, 9);
                  else if (curChar == 36)
                  {
                     if (kind > 69)
                        kind = 69;
                     jjCheckNAdd(35);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(10, 12);
                  else if (curChar == 39)
                     jjAddStates(13, 14);
                  else if (curChar == 46)
                     jjCheckNAdd(11);
                  else if (curChar == 35)
                     jjstateSet[jjnewStateCnt++] = 1;
                  if ((0x3fe000000000000L & l) != 0L)
                  {
                     if (kind > 60)
                        kind = 60;
                     jjCheckNAddTwoStates(8, 9);
                  }
                  else if (curChar == 48)
                  {
                     if (kind > 60)
                        kind = 60;
                     jjCheckNAddStates(15, 17);
                  }
                  break;
               case 56:
                  if (curChar == 42)
                     jjstateSet[jjnewStateCnt++] = 67;
                  else if (curChar == 47)
                  {
                     if (kind > 7)
                        kind = 7;
                     jjCheckNAddStates(18, 20);
                  }
                  if (curChar == 42)
                     jjCheckNAdd(62);
                  break;
               case 0:
                  if ((0x1ffffffffL & l) == 0L)
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjCheckNAdd(0);
                  break;
               case 1:
                  if (curChar == 33)
                     jjCheckNAddStates(21, 23);
                  break;
               case 2:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(21, 23);
                  break;
               case 3:
                  if ((0x2400L & l) != 0L && kind > 8)
                     kind = 8;
                  break;
               case 4:
                  if (curChar == 10 && kind > 8)
                     kind = 8;
                  break;
               case 5:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 7:
                  if ((0x3fe000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddTwoStates(8, 9);
                  break;
               case 8:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddTwoStates(8, 9);
                  break;
               case 10:
                  if (curChar == 46)
                     jjCheckNAdd(11);
                  break;
               case 11:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddStates(24, 26);
                  break;
               case 13:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(14);
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddTwoStates(14, 15);
                  break;
               case 16:
                  if (curChar == 39)
                     jjAddStates(13, 14);
                  break;
               case 17:
                  if ((0xffffff7fffffdbffL & l) != 0L)
                     jjCheckNAdd(18);
                  break;
               case 18:
                  if (curChar == 39 && kind > 66)
                     kind = 66;
                  break;
               case 20:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAdd(18);
                  break;
               case 21:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(22, 18);
                  break;
               case 22:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(18);
                  break;
               case 23:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(22);
                  break;
               case 25:
                  if (curChar == 34)
                     jjCheckNAddStates(10, 12);
                  break;
               case 26:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddStates(10, 12);
                  break;
               case 28:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(10, 12);
                  break;
               case 29:
                  if (curChar == 34 && kind > 67)
                     kind = 67;
                  break;
               case 30:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(27, 30);
                  break;
               case 31:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(10, 12);
                  break;
               case 32:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 33;
                  break;
               case 33:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(31);
                  break;
               case 34:
                  if (curChar != 36)
                     break;
                  if (kind > 69)
                     kind = 69;
                  jjCheckNAdd(35);
                  break;
               case 35:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 69)
                     kind = 69;
                  jjCheckNAdd(35);
                  break;
               case 36:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(0, 6);
                  break;
               case 37:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(37, 38);
                  break;
               case 38:
                  if (curChar != 46)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddStates(31, 33);
                  break;
               case 39:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddStates(31, 33);
                  break;
               case 41:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(42);
                  break;
               case 42:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddTwoStates(42, 15);
                  break;
               case 43:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(43, 44);
                  break;
               case 45:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(46);
                  break;
               case 46:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAddTwoStates(46, 15);
                  break;
               case 47:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(34, 36);
                  break;
               case 49:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(50);
                  break;
               case 50:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(50, 15);
                  break;
               case 51:
                  if (curChar != 48)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddStates(15, 17);
                  break;
               case 53:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddTwoStates(53, 9);
                  break;
               case 54:
                  if ((0xff000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddTwoStates(54, 9);
                  break;
               case 55:
                  if (curChar == 47)
                     jjAddStates(7, 9);
                  break;
               case 57:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddStates(18, 20);
                  break;
               case 58:
                  if ((0x2400L & l) != 0L && kind > 7)
                     kind = 7;
                  break;
               case 59:
                  if (curChar == 10 && kind > 7)
                     kind = 7;
                  break;
               case 60:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 59;
                  break;
               case 61:
                  if (curChar == 42)
                     jjCheckNAdd(62);
                  break;
               case 62:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(62, 63);
                  break;
               case 63:
                  if (curChar == 42)
                     jjCheckNAddStates(37, 39);
                  break;
               case 64:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(65, 63);
                  break;
               case 65:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(65, 63);
                  break;
               case 66:
                  if (curChar == 47 && kind > 9)
                     kind = 9;
                  break;
               case 67:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(68, 69);
                  break;
               case 68:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(68, 69);
                  break;
               case 69:
                  if (curChar == 42)
                     jjCheckNAddStates(40, 42);
                  break;
               case 70:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(71, 69);
                  break;
               case 71:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(71, 69);
                  break;
               case 72:
                  if (curChar == 47 && kind > 68)
                     kind = 68;
                  break;
               case 73:
                  if (curChar == 42)
                     jjstateSet[jjnewStateCnt++] = 67;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 6:
               case 35:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 69)
                     kind = 69;
                  jjCheckNAdd(35);
                  break;
               case 2:
                  jjAddStates(21, 23);
                  break;
               case 9:
                  if ((0x100000001000L & l) != 0L && kind > 60)
                     kind = 60;
                  break;
               case 12:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(43, 44);
                  break;
               case 15:
                  if ((0x5000000050L & l) != 0L && kind > 64)
                     kind = 64;
                  break;
               case 17:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAdd(18);
                  break;
               case 19:
                  if (curChar == 92)
                     jjAddStates(45, 47);
                  break;
               case 20:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAdd(18);
                  break;
               case 26:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(10, 12);
                  break;
               case 27:
                  if (curChar == 92)
                     jjAddStates(48, 50);
                  break;
               case 28:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(10, 12);
                  break;
               case 40:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(51, 52);
                  break;
               case 44:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(53, 54);
                  break;
               case 48:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(55, 56);
                  break;
               case 52:
                  if ((0x100000001000000L & l) != 0L)
                     jjCheckNAdd(53);
                  break;
               case 53:
                  if ((0x7e0000007eL & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddTwoStates(53, 9);
                  break;
               case 57:
                  if (kind > 7)
                     kind = 7;
                  jjAddStates(18, 20);
                  break;
               case 62:
                  jjCheckNAddTwoStates(62, 63);
                  break;
               case 64:
               case 65:
                  jjCheckNAddTwoStates(65, 63);
                  break;
               case 68:
                  jjCheckNAddTwoStates(68, 69);
                  break;
               case 70:
               case 71:
                  jjCheckNAddTwoStates(71, 69);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 6:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 6)
                        kind = 6;
                     jjCheckNAdd(0);
                  }
                  if (jjCanMove_2(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 69)
                        kind = 69;
                     jjCheckNAdd(35);
                  }
                  break;
               case 0:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjAddStates(21, 23);
                  break;
               case 17:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 26:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjAddStates(10, 12);
                  break;
               case 34:
               case 35:
                  if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 69)
                     kind = 69;
                  jjCheckNAdd(35);
                  break;
               case 57:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjAddStates(18, 20);
                  break;
               case 62:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(62, 63);
                  break;
               case 64:
               case 65:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(65, 63);
                  break;
               case 68:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(68, 69);
                  break;
               case 70:
               case 71:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(71, 69);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 74 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   37, 38, 43, 44, 47, 48, 15, 56, 61, 73, 26, 27, 29, 17, 19, 52, 
   54, 9, 57, 58, 60, 2, 3, 5, 11, 12, 15, 26, 27, 31, 29, 39, 
   40, 15, 47, 48, 15, 63, 64, 66, 69, 70, 72, 13, 14, 20, 21, 23, 
   28, 30, 32, 41, 42, 45, 46, 49, 50, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec0[i2] & l2) != 0L);
      default : 
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec0[i2] & l2) != 0L);
      default : 
         if ((jjbitVec1[i1] & l1) != 0L)
            return true;
         return false;
   }
}
private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec4[i2] & l2) != 0L);
      case 48:
         return ((jjbitVec5[i2] & l2) != 0L);
      case 49:
         return ((jjbitVec6[i2] & l2) != 0L);
      case 51:
         return ((jjbitVec7[i2] & l2) != 0L);
      case 61:
         return ((jjbitVec8[i2] & l2) != 0L);
      default : 
         if ((jjbitVec3[i1] & l1) != 0L)
            return true;
         return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, 
"\141\142\163\164\162\141\143\164", "\142\157\157\154\145\141\156", "\142\162\145\141\153", 
"\143\154\141\163\163", "\142\171\164\145", "\143\141\163\145", "\143\141\164\143\150", 
"\143\150\141\162", "\143\157\156\163\164", "\143\157\156\164\151\156\165\145", 
"\144\145\146\141\165\154\164", "\144\157", "\144\157\165\142\154\145", "\145\154\163\145", 
"\145\156\165\155", "\145\170\164\145\156\144\163", "\146\141\154\163\145", 
"\146\151\156\141\154", "\146\151\156\141\154\154\171", "\146\154\157\141\164", "\146\157\162", 
"\147\157\164\157", "\151\146", "\151\155\160\154\145\155\145\156\164\163", 
"\151\155\160\157\162\164", "\151\156\163\164\141\156\143\145\157\146", "\151\156\164", 
"\151\156\164\145\162\146\141\143\145", "\154\157\156\147", "\156\141\164\151\166\145", "\156\145\167", 
"\156\165\154\154", "\160\141\143\153\141\147\145", "\160\162\151\166\141\164\145", 
"\160\162\157\164\145\143\164\145\144", "\160\165\142\154\151\143", "\162\145\164\165\162\156", 
"\163\150\157\162\164", "\163\164\141\164\151\143", "\163\164\162\151\143\164\146\160", 
"\163\167\151\164\143\150", "\163\171\156\143\150\162\157\156\151\172\145\144", 
"\164\162\141\156\163\151\145\156\164", "\164\150\162\157\167", "\164\150\162\157\167\163", "\164\162\165\145", 
"\164\162\171", "\166\157\151\144", "\166\157\154\141\164\151\154\145", 
"\167\150\151\154\145", null, null, null, null, null, null, null, null, null, null, null, null, "\50", 
"\51", "\173", "\175", "\133", "\135", "\73", "\54", "\56", "\75", "\76", 
"\100\147\164", "\74", "\100\154\164", "\41", "\176", "\77", "\72", "\75\75", "\74\75", 
"\100\154\164\145\161", "\76\75", "\100\147\164\145\161", "\41\75", "\174\174", "\100\157\162", 
"\46\46", "\100\141\156\144", "\53\53", "\55\55", "\53", "\55", "\52", "\57", "\46", 
"\100\142\151\164\167\151\163\145\137\141\156\144", "\174", "\100\142\151\164\167\151\163\145\137\157\162", "\136", "\45", 
"\74\74", "\100\154\145\146\164\137\163\150\151\146\164", "\76\76", 
"\100\162\151\147\150\164\137\163\150\151\146\164", "\76\76\76", 
"\100\162\151\147\150\164\137\165\156\163\151\147\156\145\144\137\163\150\151\146\164", "\53\75", "\55\75", "\52\75", "\57\75", "\46\75", 
"\100\141\156\144\137\141\163\163\151\147\156", "\174\75", "\100\157\162\137\141\163\163\151\147\156", "\136\75", "\45\75", 
"\74\74\75", "\100\154\145\146\164\137\163\150\151\146\164\137\141\163\163\151\147\156", 
"\76\76\75", 
"\100\162\151\147\150\164\137\163\150\151\146\164\137\141\163\163\151\147\156", "\76\76\76\75", 
"\100\162\151\147\150\164\137\165\156\163\151\147\156\145\144\137\163\150\151\146\164\137\141\163\163\151\147\156", };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x1ffffffffffffc01L, 0xffffffffffffff3dL, 0x3fL, 
};
static final long[] jjtoSkip = {
   0x3feL, 0x0L, 0x0L, 
};
static final long[] jjtoSpecial = {
   0x380L, 0x0L, 0x0L, 
};
protected JavaCharStream input_stream;
private final int[] jjrounds = new int[74];
private final int[] jjstateSet = new int[148];
protected char curChar;
public ParserTokenManager(JavaCharStream stream)
{
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public ParserTokenManager(JavaCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 74; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         matchedToken.specialToken = specialToken;
         return matchedToken;
      }
      else
      {
         if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
         {
            matchedToken = jjFillToken();
            if (specialToken == null)
               specialToken = matchedToken;
            else
            {
               matchedToken.specialToken = specialToken;
               specialToken = (specialToken.next = matchedToken);
            }
         }
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
