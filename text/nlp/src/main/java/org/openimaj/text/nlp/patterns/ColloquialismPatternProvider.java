/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.text.nlp.patterns;

import java.util.Arrays;
import java.util.Comparator;

import org.openimaj.text.util.RegexUtil;


public class ColloquialismPatternProvider extends PatternProvider {
	String[] EmoticonsDNArr = new String[] {
			"2day","2morrow","2nite","2night"
		};
	
	String EmoticonsDN = RegexUtil.regex_or_match(longestfirst(EmoticonsDNArr));
//	String EmoticonsDN = regex_or(EmoticonsDNArr);
	
	@Override
	public String patternString(){
		return EmoticonsDN;
	}

	private String[] longestfirst(String[] emoticons) {
		Arrays.sort(emoticons,new Comparator<String>(){

			@Override
			public int compare(String s1, String s2) {
				int s1Longer = s1.length() - s2.length();
				if(s1Longer > 0) return -1;
				else if(s1Longer < 0) return 1;
				else{
					return s1.compareTo(s2);
				}
			}
			
		});
		return emoticons;
	}

	@Override
	public PatternProvider combine(PatternProvider other) {
		return new CombinedPatternProvider(this,other);
	}
}