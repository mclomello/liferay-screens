/**
* Copyright (c) 2000-present Liferay, Inc. All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
* details.
*/
import UIKit
import LiferayScreens

class SignUpViewController: CardViewController, SignUpScreenletDelegate {

	@IBOutlet weak var screenlet: SignUpScreenlet?


	//MARK: Init methods

	convenience init() {
		self.init(nibName: "SignUpViewController", bundle: nil)
	}


	//MARK: UIViewController
	
	override func viewDidLoad() {
		self.screenlet?.delegate = self

		self.screenlet?.anonymousApiUserName =
				LiferayServerContext.propertyForKey("anonymousUsername") as? String
		self.screenlet?.anonymousApiPassword =
				LiferayServerContext.propertyForKey("anonymousPassword") as? String
	}


	//MARK: SignUpScreenletDelegate

	func screenlet(screenlet: SignUpScreenlet,
			onSignUpResponseUserAttributes attributes: [String:AnyObject]) {
		onDone?()
	}
	

}
