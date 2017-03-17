package org.ogai.command;

import org.ogai.core.Ctx;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.view.View;
import org.ogai.view.html.HTMLView;

/**
 * Абстрактная команда.
 * Содержит абстрактную имплементацию метода интерфейса Executable execute которая
 * обрабатывает ошибку и для получения View в случае их возниконвоения вызывает абстрактные методы
 * getErrorView
 *
 * @author Побединский Евгений
 *         23.03.14 17:39
 */
public abstract class AbstractCommand implements Executable {
	protected static Log log = LogFactory.create(AbstractCommand.class);

	public View execute() throws OgaiException {
		View view = null;
		try {
			//Проверяем доступна ли данная комманда этому пользователю
			if (!isAccess()) {
				view = noAccessView();
			} else {
				view = doExecute(new HTMLView());
			}
		} catch (OgaiException oe) {
			log.error("Exception during Command execution:" + Ctx.get().getRequestParams()
					+ ";" + Ctx.get().getUser() + " : " + oe.getMessage(), oe);
			view = getErrorView(oe);
		} catch (Exception e) {
			log.error("Unknown exception during Command execution:" + Ctx.get().getRequestParams()
					+ ";" + Ctx.get().getUser() + " : " + e.getMessage(), e);
			view = getErrorView(e);
		}

		return view;
	}

	/**
	 * Здесь должна быть выполнена основная работа команды.
	 * Все ошибки не должны обрабатываться в общем случае в этом методе, они буду обработаны в
	 * коде родительского класса
	 * @param view в который заносим ответ этой команды клиенту
	 * @return view
	 * @throws OgaiException
	 */
	protected abstract View doExecute(View view) throws OgaiException;

	/**
	 *
	 * @param e
	 * @return View в случае если возникла наша ошибка
	 */
	protected abstract View getErrorView(OgaiException e);
	/**
	 *
	 * @param e
	 * @return View в случае если возникла некоторая неожиданная ошибка
	 */
	protected abstract View getErrorView(Exception e);

	/**
	 * Показать пользователю, что он не имеет прав квыполнению этой команды
	 * @return
	 */
	protected abstract View noAccessView();

	/**
	 *
	 * @return true если эта команда доступна текущему пользователю
	 */
	protected boolean isAccess() {
		//Пока просто проверяем, что текущий пользователь не гость
		return (!Ctx.get().isGuest());
	}
}
