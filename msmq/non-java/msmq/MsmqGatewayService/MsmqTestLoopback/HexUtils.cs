using System;
using System.Collections.Generic;
using System.Text;

namespace MsmqTestLoopback
{
    class HexUtils
    {
        public static string FullHexFormat(byte[] data)
        {
            StringBuilder builder = new StringBuilder();

            for (int row = 0; row * 16 < data.Length; row++)
            {
                builder.AppendFormat("{0:X6} : ", row * 16);
                for (int col = 0; col < 16 ; col++)
                {
                    if ( row * 16 + col < data.Length ) 
                        builder.AppendFormat("{0:X2} ", data[row * 16 + col]);
                    else 
                        builder.Append("   ");
                }
                builder.Append("    ");
                for (int col = 0; col < 16; col++)
                {
                    if (row * 16 + col < data.Length)
                    {
                        byte b = data[row * 16 + col];

                        if (b >= 32 && b < 128)
                            builder.Append((char)b);
                        else
                            builder.Append('.');
                    }
                    else
                        builder.Append(' ');
                }
                builder.AppendLine();
            }

            return builder.ToString();
        }
    }
}
