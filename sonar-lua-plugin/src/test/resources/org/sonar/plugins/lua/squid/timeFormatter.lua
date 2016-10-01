--[[
     * Convert date and time to a string
     * 
     * <p>The formats allowed follow the C-style syntax of strftime(). See
     * http://www.opengroup.org/onlinepubs/007908799/xsh/strftime.html
     * for details. Note: not all formats are currently implemented, I have
     * only done those ones that were easy to implement using the Date
     * object.</p>
     * 
     * <p>Each conversion specification in the format string is represented 
     * by a percent character "%" followed by a specific character as per 
     * the following:</p>
     * 
     * <p> %a is replaced by the abbreviated weekday name (Mon, Tue, etc)</p>
     * 
     * <p> %A is replaced by the full weekday name.</p>
     * 
     * <p> %b is replaced by the abbreviated month name.</p>
     * 
     * <p> %B is replaced by the full month name.</p>
     * 
     * <p> %c is replaced by the default Date.toString() value.</p> 
     * 
     * <p> %C is replaced by the century number (the year divided by 100 and
     * truncated to an integer) as a decimal number [00-99].</p>
     * 
     * <p> %d is replaced by the day of the month as a decimal number
     * [01-31].</p>
     * 
     * <p> %D is the same as %d/%m/%y (deviation from standard).</p>
     * 
     * <p> %e is replaced by the day of the month as a decimal number [1-31];
     * a single digit is preceded by a space.</p>
     * 
     * <p> %e is replaced by the day of the month as a decimal number [1-31];
     * no leading zero or space character (non-standard addition).</p>
     * 
     * <p> %f gives the ordinal suffix (st, nd, rd, th) appropriate to the
     * day of the month (non-standard addition).</p>
     * 
     * <p> %h is the same as %b. 
     * 
     * <p> %H is replaced by the hour (24-hour clock) as a decimal number
     * [00-23].</p>
     * 
     * <p> %I is replaced by the hour (12-hour clock) as a decimal number
     * [1-12].</p>
     * 
     * <p> %j is replaced by the day of the year as a decimal number
     * [001-366].</p>
     * 
     * <p> %m is replaced by the month as a decimal number [01-12].</p>
     * 
     * <p> %M is replaced by the minute as a decimal number [00-59].</p> 
     * 
     * <p> %n is replaced by a newline character.</p> 
     * 
     * <p> %p is replaced by either a.m. or p.m.</p>
     * 
     * <p> %P is replaced by either am or pm (non-standard addition)</p>
     * 
     * <p> %r is equivalent to %I:%M:%S %p.</p>
     * 
     * <p> %R is replaced by the time in 24 hour notation (%H:%M).</p>
     * 
     * <p> %S is replaced by the second as a decimal number [00-61].</p>
     * 
     * <p> %t is replaced by a tab character.</p>
     * 
     * <p> %T is replaced by the time (%H:%M:%S).</p>
     * 
     * <p> %u is replaced by the weekday as a decimal number [1-7],
     * with 1 representing Monday.</p>
     * 
     * <p> %U is not implemented.</p>
     * 
     * <p> %V is not implemented.</p>
     * 
     * <p> %w is replaced by the weekday as a decimal number [0-6],
     * with 0 representing Sunday.</p>
     * 
     * <p> %W is not implemented.</p>
     * 
     * <p> %x is not implemented.</p>
     * 
     * <p> %X is not implemented.</p>
     * 
     * <p> %y is replaced by the year without century as a decimal number
     * [00-99].</p>
     * 
     * <p> %Y is replaced by the year with century as a decimal number.</p>
     * 
     * <p> %Z is replaced by the timezone name or abbreviation, or by no bytes
     * if no timezone information exists.</p>
     * 
     * <p> %% is replaced by literal %.</p>
     * 
     * @param format The format to use.
     * @param date The date to format.
     */--]]
