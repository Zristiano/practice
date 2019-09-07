package yuanmengzeng.practice;

import java.util.*;

/**
 * Q1
 * <p>
 * Given a string, find the length of the longest substring without repeating characters.
 * </p>
 * Example:
 * <p>
 * Input: "abcabcbb"
 * </p>
 * Output: 3
 * <p>
 * Explanation: The answer is "abc", which the length is 3.
 * </p>
 */
public class StringQuestion {

    /**
     * <p>
     * Leetcode 3
     * </p>
     * principle: the longest substring without duplicate character has the same character on its both borders, which means
     * when coming across a character that already exists in once iteration, we could count a new substring from the index of
     * the existed character.
     */
    public int lengthOfLongestSubstring(String s){
        int maxSub = 0;
        int subStringLeft = 0;
        HashMap<Character,Integer> charMap = new HashMap<>();
        for (int i =0;i<s.length();i++){
            char curChar = s.charAt(i);
            // if there is a same character in the map, we remove it and find its position A,
            // then we recount the max length of subString starting from index A+1
            if (charMap.containsKey(curChar)){
                int existPos = charMap.remove(curChar);
                subStringLeft = Math.max(existPos,subStringLeft);
            }
            charMap.put(curChar,i+1);
            maxSub = Math.max(maxSub,i-subStringLeft+1);
        }
        return maxSub;
    }

    /**
     * Leetcode 3
     * <p>
     *     using ascii table to map the character and array
     * </p>
     * principle: the longest substring without duplicate character has the same character on its both borders, which means
     * when coming across a character that already exists in once iteration, we could count a new substring from the index of
     * the existed character.
     */
    public int lengthOfSubstringUseAscii(String s){
        int[] chars = new int[128];
        int maxSub = 0;
        int left = 0;
        for (int i =0; i<s.length();i++){
            char curChar = s.charAt(i);
            // now chars[curChar] represents the index of 'curChar' we have before, if chars[curChar] >= left,
            // it means we come across a duplicate character in the current substring, then we start count a
            // new substring from the index+1.
            left = Math.max(left,chars[curChar]);
            chars[curChar] = i+1;
            maxSub = Math.max(maxSub,i-left+1);
        }
        return maxSub;
    }

    public String longestPalindrome(String s) {
        if(s==null || s.length()==0){
            return null;
        }
        if (s.length()==1){
            return s;
        }
        int longestStart=0, longestEnd=0;
        ArrayList<Integer> curPalindromePos = new ArrayList<>();
        for(int i=1; i<s.length();i++){
            curPalindromePos.add(i+1);
            if(s.charAt(i)==s.charAt(i-1)){
                curPalindromePos.add(i);
            }
            int left = 0, right =0;
            for(int j=curPalindromePos.size()-1;j>=0;j--){
                int pos = curPalindromePos.get(j);
                if(pos==0){
                    curPalindromePos.remove(j);
                    continue;
                }else {
                    if(s.charAt(pos-1)==s.charAt(i)){
                        curPalindromePos.set(j,pos-1);
                        left = pos-1;
                    }else {
                        curPalindromePos.remove(j);
                        continue;
                    }
                }
                right = i;
                if((right - left)>(longestEnd-longestStart)){
                    longestEnd = right;
                    longestStart = left;
                }
            }
        }
        return s.substring(longestStart,longestEnd+1);
    }

    public String longestPalindromeManacher(String s){
        if(s==null || s.length()==0){
            return "";
        }
        StringBuilder sbuilder = new StringBuilder("#");
        for(int i=0; i<s.length(); i++){
            sbuilder.append(s.charAt(i)).append("#");
        }
        String extS = sbuilder.toString();
        int[] curPal = new int[extS.length()];
        int center = 0;
        int maxPos = 0;
        for(int i=1; i<extS.length(); i++){
            if(curPal[center]-1+center<=i){
                curPal[i] = getCurPosPalindrome(extS, i);
            }else{
                int k = i-center;
                int coLeft = center-k;
                if(coLeft - curPal[coLeft]>center-curPal[center]){
                    curPal[i] = curPal[coLeft];
                }else if(coLeft - curPal[coLeft]<center-curPal[center]){
                    curPal[i] = coLeft - (center-curPal[center]);
                }else {
                    curPal[i] = getCurPosPalindrome(extS,i);
                }
            }
            if(i+curPal[i]>center+curPal[center]){
                center = i;
            }
            if(curPal[maxPos]<curPal[i]){
                maxPos = i;
            }
        }
        String subExtS = extS.substring(maxPos-curPal[maxPos]+1,maxPos+curPal[maxPos]);
        return subExtS.replace("#","");
    }

    private int getCurPosPalindrome(String s, int pos){
        int pal = 0;
        for(int i=0; pos-i>=0&&pos+i<s.length();i++){
            if(s.charAt(pos-i)!=s.charAt(pos+i)){
                break;
            }
            pal++;
        }
        return pal;
    }
    public String convert(String s, int numRows) {
        ArrayList<ArrayList<Character>> letters = new ArrayList<>();
        for(int i =0; i<numRows; i++){
            letters.add(new ArrayList<>());
        }
        boolean isDownward = false;
        int row =0;
        int colum = 0;
        for(int i=0;i<s.length();i++){
            int remain = colum%(numRows-1);
            if(remain == 0){
                letters.get(row).add(s.charAt(i));
                row++;
                if(row==numRows){
                    colum++;
                }
            }else {
                row = numRows-1-remain;
                letters.get(row).add(s.charAt(i));
                colum++;
                row=0;
            }
        }
        StringBuilder sb = new StringBuilder();
        for(ArrayList<Character> rowChar : letters){
            for(Character letter : rowChar){
                sb.append(letter);
            }
        }
        return sb.toString();
    }


    public boolean isMatch(String s, String p) {
        if(s==null || p == null){
            return false;
        }
        int sLen = s.length();
        int pLen = p.length();
        // boolean[0][m] represents match result when s is empty; boolean[n][0] represents match result when p is empty
        boolean[][] match = new boolean[sLen+1][pLen+1];
        match[0][0] = true; // s="", p=""
        for(int i=2; i<pLen+1; i+=2){
            // s="" , p!=""
            match[0][i] = (p.charAt(i-1)=='*' && match[0][i-2]  && (p.charAt(i-2)=='.' || p.charAt(i-2)>='a' && p.charAt(i-2)<='z'));
        }

        if(pLen>0 && sLen>0 && (p.charAt(0)=='.' || p.charAt(0)==s.charAt(0))){
            match[1][1] = true;
        }

        for(int si = 1; si<sLen+1; si++){
            for(int pi = 2; pi< pLen+1; pi++){
                char pc = p.charAt(pi-1);
                char sc = s.charAt(si-1);
                char ppc = p.charAt(pi-2);
                if(pc==sc || pc=='.'){
                    match[si][pi] = match[si-1][pi-1];
                }else if(pc=='*'){
                    if(sc==ppc || ppc=='.'){
                        match[si][pi]=match[si-1][pi];
                    }else{
                        match[si][pi]=match[si][pi-2];
                    }
                }
            }
        }
        return match[sLen][pLen];
    }

    public List<String> generateParenthesis(int n) {
        if(n==0){
            return new ArrayList<>();
        }
        HashSet<String> lastSet = new HashSet<>();
        HashSet<String> nowSet = new HashSet<>();
        lastSet.add("()");

        for(int i=1; i<n; i++){
            for (String s:lastSet){
                for (int j=0; j<s.length(); j++){
                    StringBuilder sb = new StringBuilder("("+s);
                    sb.insert(j+1,')');
                    nowSet.add(sb.toString());
                }
            }
            HashSet<String> temp = nowSet;
            nowSet = lastSet;
            lastSet = temp;
            nowSet.clear();
        }
        return new ArrayList<>(lastSet);
    }

    /**
     * leetcode 22
     */
    public List<String> generateParenthesisByBackTracing(int n) {
        List<String> list = new ArrayList<>();
        backTracing(0,0,"",n,list);
        return list;
    }

    private void backTracing(int left, int right, String s, int n, List<String> list){
        if(left==n && right==n){
            list.add(s);
            return;
        }
        if (left<n){
            backTracing(left+1,right,s+"(",n,list);
        }
        if (left>right){
            backTracing(left,right+1,s+")",n,list);
        }
    }

    /**
     *
     * leetcode 30
     */
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (s==null || s.length()==0 || words==null || words.length==0){
            return  result;
        }
        if (words[0].length()==0){
            for (int i=0; i<=s.length();i++){
                result.add(i);
            }
            return result;
        }
        HashMap<String, Integer> wordsFreq = new HashMap<>();
        for (String word: words){
            wordsFreq.put(word,wordsFreq.getOrDefault(word,0)+1);
        }
        int wordLen = words[0].length();
        int wordNum = words.length;
        HashMap<String, Integer> curWordFreq;
        for(int i=0; i<wordLen; i++){
            int count = 0;
            int left = i;
            curWordFreq = new HashMap<>();
            for(int j=i; j<=s.length()-wordLen; j+=wordLen){
                String subS = s.substring(j,j+wordLen);
                if (wordsFreq.containsKey(subS)){
                    curWordFreq.put(subS,curWordFreq.getOrDefault(subS,0)+1);
                    count++;
                    if(curWordFreq.get(subS)<=wordsFreq.get(subS)){
                        if(count == wordNum){
                            result.add(left);
                            continue;
                        }
                        if(count<wordNum) continue;
                    }
                    while(curWordFreq.get(subS)>wordsFreq.get(subS)){
                        String leftString = s.substring(left,left+wordLen);
                        curWordFreq.put(leftString,curWordFreq.get(leftString)-1);
                        left+=wordLen;
                        count--;
                    }
                    if (count==wordNum){
                        result.add(left);
                    }
                }else{
                    left = j+wordLen;
                    count = 0;
                    curWordFreq.clear();
                }

            }
        }
        return result;
    }

    /**
     * leetcode 32
     */
    public int longestValidParentheses(String s) {
        int dp[] = new int[s.length()];
        int maxLen=0;
        for(int i =1; i<s.length(); i++){
            if(s.charAt(i)==')'){
                if(s.charAt(i-1)=='('){
                    dp[i]=2;
                }else{
                    int shift = 0;
                    //  (((())()))))()  (((())()))(()))()
                    //  s:    (  (  (  (  )  )  (  )  )  )  )  )  (  )
                    //  i:    0  1  2  3  4  5  6  7  8  9  10 11 12 13
                    // dp[]:  0  0  0  0  2  4  0  2  8  10 0  0  0  2
                    while(i-shift-1>=0 &&s.charAt(i-shift-1)==')'){
                        if (dp[i-shift-1]==0){
                            break;
                        }else{
                            shift+=dp[i-shift-1];
                        }
                    }
                    if(i-shift-1>=0){
                        // the left char is '('
                        if( s.charAt(i-shift-1)=='('){
                            dp[i]=shift+2;
                        }
                    }
                }
            }
        }

        int k = dp.length-1;
        int curLen = 0;
        while(k>=0){
            if(dp[k]>0){
                curLen+=dp[k];
                maxLen = maxLen< curLen? curLen:maxLen;
                k-=dp[k];
            }else{
                curLen = 0 ;
                k--;
            }
        }

        return maxLen;
    }

    /**
     * find subString in length num with only 1 repeated character
     * @param inputString s
     * @param num length
     * @return list
     * example： "1231245124"   num=5
     * return: [23124, 12451, 24512, 45124]
     */
    public  List<String> findK(String inputString, int num){
        List<String> ans = new ArrayList<>();
        if (num>inputString.length()){
            return ans;
        }
        char[] chars = new char[256];
        int countDup = 0;
        for (int i=0;i<num; i++){
            char c = inputString.charAt(i);
            chars[c]++;
            if (chars[c]>=2) countDup ++;
        }
        if (countDup==1) ans.add(inputString.substring(0,num));
        int l = 0;
        while(l<inputString.length()-num){
            char prev = inputString.charAt(l);
            char next = inputString.charAt(l+num);
            chars[prev]--;
            if (chars[prev]>=1) countDup --;
            chars[next]++;
            if (chars[next]>=2) countDup ++;
            l++;
            if (countDup==1) ans.add(inputString.substring(l,l+num));
        }
        return ans;
    }
    //23280674775041

    public static void parseConfiguration(List<String> configurationLines) {
        // Your code here. Writes to standard output.
        TreeMap<String,TreeMap<String,String>> configMap = new TreeMap<>();
        HashMap<String,String> extendMap = new HashMap<>();
        TreeMap<String,String> keyValues = null;
        String id = "";
        // time: O(n)  space: O(n)
        for(int i=0; i<configurationLines.size(); i++){
            String line = configurationLines.get(i);
            if(line.startsWith("[")){
                line = line.replace("[","");
                line = line.replace("]","");
                String[] ids = line.split(":");
                if(ids.length==2){
                    extendMap.put(ids[0],ids[1]);
                }else{
                    extendMap.put(ids[0],"");
                }
                id = ids[0];
                keyValues = new TreeMap<>();
                configMap.put(id,keyValues);
            }else if(line!=null && line.length()>0){
                String[] confs = line.split(" = ");
                keyValues.put(confs[0],confs[1]);
                configMap.put(id,keyValues);
            }
        }
        Iterator<String> keyIt = extendMap.keySet().iterator();
        HashSet<String> changedIdConfigSet = new HashSet<>();
        // time: o(2n)  space: O(kn)
        while (keyIt.hasNext()){
            String idKey = keyIt.next();
            if (changedIdConfigSet.contains(idKey)){
                continue;
            }
            List<String> idString = new ArrayList<>();
            idString.add(idKey);
            changedIdConfigSet.add(idKey);
            while (!extendMap.get(idKey).equals("")){
                idKey = extendMap.get(idKey);
                idString.add(idKey);
                changedIdConfigSet.add(idKey);
            }
            TreeMap<String,String> lastIdConfs = configMap.get(idString.get(idString.size()-1));
            for (int i= idString.size()-2; i>=0; i--) {
                TreeMap<String, String> idConfs = configMap.get(idString.get(i));
                Iterator<String> configKeyIt = lastIdConfs.keySet().iterator();
                while (configKeyIt.hasNext()) {
                    String configKey = configKeyIt.next();
                    if (!idConfs.containsKey(configKey)) {
                        idConfs.put(configKey, lastIdConfs.get(configKey));
                    }
                }
                lastIdConfs = idConfs;
            }
        }
        Iterator<String> idIt = configMap.keySet().iterator();
        while (idIt.hasNext()){
            String idConfig = idIt.next();
            System.out.println("["+idConfig+"]");
            keyValues = configMap.get(idConfig);
            keyIt = keyValues.keySet().iterator();
            while (keyIt.hasNext()){
                String key = keyIt.next();
                System.out.println(key+" = "+keyValues.get(key));
            }
            System.out.println("");
        }
//        System.out.println(""+ extendMap);
//        System.out.println(""+ configMap);
    }

    public int minDeletionSize(String[] A) {
        List<List<String>> list = new ArrayList<>();
        List<String> subL = new ArrayList<>(Arrays.asList(A));
        list.add(subL);
        int count = 0;
        List<List<String>> newList = new ArrayList<>(list);
        search :for(int n = 0; n<A[0].length(); n++){
            List<List<String>> temp = newList;
            newList = list;
            list = temp;
            newList.clear();
            for(int i=0; i<list.size(); i++){
                List<String> curl = list.get(i);
                int curIdx = -1;
                List<String> toBeCheckList = new ArrayList<>();
                for(int j=0; j<curl.size(); j++){
                    int cIdx = curl.get(j).charAt(n)-'a';
                    if(cIdx>curIdx){
                        if(toBeCheckList.size()>0){
                            newList.add(toBeCheckList);
                            toBeCheckList = new ArrayList<>();
                        }
                        curIdx = cIdx;
                    }else if(cIdx==curIdx){
                        if(toBeCheckList.size()==0){
                            toBeCheckList.add(curl.get(j-1));
                        }
                        toBeCheckList.add(curl.get(j));
                    }else{
                        count++;
                        newList.clear();
                        newList.addAll(list);
                        continue search;
                    }
                }
                if(toBeCheckList.size()>0){
                    newList.add(toBeCheckList);
                }
            }
        }
        return count;
    }

    public int atoi(String s){
        if (s==null || s.isEmpty()){
            throw new IllegalArgumentException("parameter is not an integer");
        }
        int sign = 1;
        long res = 0;
        char[] chars = s.toCharArray();
        int i = 0;
        while(i<chars.length && (chars[i]=='+'||chars[i]=='-')){
            if (chars[i]=='-') sign*=-1;
            i++;
        }
        boolean start = false;
        while(i<chars.length && chars[i]>='0' && chars[i]<='9'){
            start = true;
            res = res*10+(chars[i]-'0');
            i++;
        }
        if (start && i==chars.length && res<=Integer.MAX_VALUE && res>=Integer.MIN_VALUE){
            return (int)(sign*res);
        }
        throw new IllegalArgumentException("parameter is not an integer");
    }


    /**
     * Parenthesis matching with '*', '*' can be used as '('  ')'  ''
     * <p>
     *   Example:
     *   <br> (((*)) -> true
     *   <br> ((*** -> true
     *   <br> (()))* -> false
     * <p/>
     */
    public boolean validStarParenthesis(String string){
        int l = 0, r =0;
        int starCnt = 0;
        char[] s = string.toCharArray();
        while(r<s.length){
            if(s[r]!=')'){
                r++;
            }else{
                if(l<r){
                    l++;
                    r++;
                    while (l<r && s[l]!='('){
                        if (s[l] == '*') starCnt++;
                        l++;
                    }
                }else if (starCnt>0){
                    starCnt--;
                    r++;
                    while (l<r && s[l]!='('){
                        if (s[l] == '*') starCnt++;
                        l++;
                    }
                }else{
                    return false;
                }
            }
        }
        if (l<s.length){
            int leftPaCnt = 0;
            for (;l<s.length;l++){
                if(s[l]=='(') leftPaCnt++;
                else if (s[l]=='*' && leftPaCnt>0) leftPaCnt--;
            }
            return leftPaCnt==0;
        }
        return true;
    }

    public int countPalindrome(String s){
        int count = 0;
        char[] chars = s.toCharArray();
        for(int i=0; i< s.length()-1; i++){
            count+=numsOfPalindrome(chars,i,i);
            count+=numsOfPalindrome(chars,i,i+1);
        }
        return count + 1;
    }

    private int numsOfPalindrome(char[] array, int i, int j){
        int num = 0;
        while(i>=0 && j<array.length && array[i] == array[j]){
            num++;
            i--;
            j++;
        }
        return num;
    }


    public List<String> drainUpperLowerCombination(String s){
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dfs(s.toCharArray(), sb, res, 0, 'a'-'A');
        return res;
    }

    private void dfs(char[] chars, StringBuilder sb, List<String> res, int i, int dis) {
        if (i == chars.length) {
            res.add(sb.toString());
            return;
        }
        char lowerCase, upperCase;
        if ('a' <= chars[i] && chars[i] <= 'z') {
            lowerCase = chars[i];
            upperCase = (char) (lowerCase - dis);
            sb.append(lowerCase);
            dfs(chars, sb, res, i + 1, dis);
            sb.deleteCharAt(i);

            sb.append(upperCase);
            dfs(chars, sb, res, i + 1, dis);
            sb.deleteCharAt(i);

        } else if ('A' <= chars[i] && chars[i] <= 'Z') {
            lowerCase = (char) (chars[i] + dis);
            upperCase = chars[i];
            sb.append(lowerCase);
            dfs(chars, sb, res, i + 1, dis);
            sb.deleteCharAt(i);

            sb.append(upperCase);
            dfs(chars, sb, res, i + 1, dis);
            sb.deleteCharAt(i);
        } else {
            sb.append(chars[i]);
            dfs(chars, sb, res, i + 1, dis);
            sb.deleteCharAt(i);
        }
    }
    public List<String> missingWord(String s, String t){
        s = s.trim();
        t = t.trim();
        List<String> res = new LinkedList<>();
        String[] ss = s.split(" ");
        String[] ts = t.split(" ");
        if(ss.length==0 || ts.length==0) return res;
        int j = 0 ;
        for (String first : ss){
            if (j<ts.length && first.equals(ts[j])){
                j++;
            }else {
                res.add(first);
            }
        }
        return res;
    }


    public String[] subString(String s){
        Character[] vowelArray = {'a','e','i','o','u'};
        String[] res = new String[2];
        Set<Character> vowels = new HashSet<>(Arrays.asList(vowelArray));
        res[0] = "z";
        res[1] = "a";
        char[] sChars = s.toCharArray();
        int lastConsonantIdx = sChars.length-1;
        while (lastConsonantIdx>=0 && vowels.contains(sChars[lastConsonantIdx])) lastConsonantIdx--;
        if (lastConsonantIdx<=0) return res;

        // find out the smallest substring
        List<int[]> smallestIdxs = new ArrayList<>();
        int nearestConsonantIdx = lastConsonantIdx;
        char smallestVowel = 'z';
        char nearestDiffChar = sChars[lastConsonantIdx];
        for(int i=lastConsonantIdx ; i>=0; i--){
            if (vowels.contains(sChars[i])){
                if(sChars[i]<smallestVowel){
                    smallestVowel = sChars[i];
                    smallestIdxs.clear();
                    smallestIdxs.add(new int[]{i,nearestConsonantIdx+1});
                    if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
                }else if (sChars[i]==smallestVowel){
                    // if the next following character is the same vowel as current one
                    if(sChars[i+1]==smallestVowel){
                        if(smallestVowel<nearestDiffChar) {
                            smallestIdxs.get(smallestIdxs.size() - 1)[0] = i;
                        }
                    }else {
                        smallestIdxs.add(new int[]{i, nearestConsonantIdx+1});
                        if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
                    }
                }
            }else {
                nearestConsonantIdx = i;
                if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
            }
        }
        for (int[] subStringBoudry : smallestIdxs){
            String string = s.substring(subStringBoudry[0], subStringBoudry[1]);
            System.out.println("smallest vowel idx:"+subStringBoudry[0] + "   consonant idx:"+subStringBoudry[1]+"  subString : "+string);
            res[0] = res[0].compareTo(string)<=0 ? res[0] : string;
        }


        // find out the largest substring
        char largestVowel = 'a'-1 ;
        nearestDiffChar = sChars[lastConsonantIdx];
        List<Integer> largestVowelIdxs = new ArrayList<>();
        for (int i=lastConsonantIdx; i>=0; i--){
            if (vowels.contains(sChars[i])){
                if (sChars[i]>largestVowel){
                    largestVowel = sChars[i];
                    largestVowelIdxs.clear();
                    largestVowelIdxs.add(i);
                    if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
                }else if (sChars[i]==largestVowel){
                    // if the next following character is the same vowel as current one
                    if (sChars[i+1]==largestVowel){
                        if (largestVowel > nearestDiffChar){
                            largestVowelIdxs.remove(largestVowelIdxs.size()-1);
                            largestVowelIdxs.add(i);
                        }
                    }else {
                        largestVowelIdxs.add(i);
                        if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
                    }
                }
            }else {
                if(i+1<sChars.length) nearestDiffChar = sChars[i+1];
            }
        }
        for (Integer idx : largestVowelIdxs){
            System.out.println("largest idx:"+idx);
            String string = s.substring(idx, lastConsonantIdx+1);
            res[1] = res[1].compareTo(string)>0 ? res[1] : string;
        }
        return res;
    }


    public String[] subStringLuo(String s) {
//        char[] vo = {'a', 'e','i','o','u'};
//        char[] con = {'b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','y','z'};
//
        Character[] vo = {'a','e','i','o','u'};
        Set<Character> vset = new HashSet<Character>(Arrays.asList(vo));
        char[] scarr = s.toCharArray();
        int lastchar = scarr.length - 1;
        while(lastchar >= 0 && vset.contains(scarr[lastchar])) {
            lastchar--;
        }
        String highest = "";
        for(int i = 0; i < lastchar; i++) {
            if(vset.contains(scarr[i])) {
                //optimization
                while (i < lastchar - 1 && scarr[i] == scarr[i + 1] && scarr[i] < scarr[lastchar]) {
                    i++;
                }
                System.out.println("largest i="+i);
                String temp = s.substring(i, lastchar + 1);
                if(highest.compareTo(temp) < 0) {
                    highest = temp;
                }
            }
        }
        String lowest = s.substring(0, lastchar + 1);
        int vostart = 0;
        int conend = 1;
        while(conend <= lastchar && vset.contains(scarr[conend])) {
            conend++;
        }
        while(vostart <= conend && conend <= lastchar) {
            //optimization
            while(vostart < lastchar && !vset.contains(scarr[vostart])) {
                vostart++;
            }
            if(vostart == lastchar) {
                break;
            }
            if(conend < vostart){
                conend = vostart + 1;
                while(conend < lastchar && vset.contains(scarr[conend])) {
                    conend++;
                }
            }
            System.out.println("smallest i="+vostart);
            String temp = s.substring(vostart, conend + 1);
            if(lowest.compareTo(temp) > 0) {
                lowest = temp;
            }
            while (vostart < conend - 1 && scarr[vostart] == scarr[vostart+1] && scarr[vostart] < scarr[conend]) {
                vostart++;
            }
            vostart++;

        }
        return new String[]{lowest, highest};
    }
}
