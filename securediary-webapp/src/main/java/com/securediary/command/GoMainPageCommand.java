package com.securediary.command;

import com.securediary.scramble.Scrambler;
import com.securediary.scramble.SimpleScrambler;
import org.ogai.command.HTMLCommand;
import org.ogai.exception.OgaiException;
import org.ogai.view.RedirectView;
import org.ogai.view.View;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         13.06.14 17:19
 */
public class GoMainPageCommand extends HTMLCommand {
	public static final String NAME = "gomain";

	@Override
	protected View doExecute(View view) throws OgaiException {
		return new RedirectView("mainPage.jsp");
	}
}
