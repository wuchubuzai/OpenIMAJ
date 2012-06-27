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
package org.openimaj.citation.annotation.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.openimaj.citation.annotation.Reference;
import org.openimaj.citation.annotation.References;
import org.openimaj.citation.annotation.output.StandardFormatters;

/**
 * {@link Processor} implementation that is capable of finding
 * {@link Reference} and {@link References} annotations and generating
 * lists which are then written.
 * 
 * Currently the processor produces a BibTeX bibliography containing all
 * references in the project.
 * 
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 */
@SupportedAnnotationTypes(value = { "org.openimaj.citation.annotation.Reference", "org.openimaj.citation.annotation.References" })
public class ReferenceProcessor extends AbstractProcessor {
	Set<Reference> references = new HashSet<Reference>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		for (TypeElement te : annotations) {
			for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
				Reference ann1 = e.getAnnotation(Reference.class);
				if (ann1 != null) {
					references.add(ann1);
				}

				References ann2 = e.getAnnotation(References.class);
				if (ann2 != null) {
					for (Reference r : ann2.references()) {
						references.add(r);
					}
				}
			}
		}

		if (roundEnv.processingOver()) {
			processingEnv.getMessager().printMessage(Kind.NOTE, "Creating project bibliography");
			
			String bibtex = StandardFormatters.BIBTEX.formatReferences(references);
			try {
				FileObject file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "bibliography.bib");

				Writer writer = new PrintWriter(file.openOutputStream());
				writer.append(bibtex);
				writer.close();

			} catch (IOException e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Error writing bibtex " + e);
			}
		}
		
		return true;
	}

}