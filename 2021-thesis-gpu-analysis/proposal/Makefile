pdf:	clean 
	xelatex -interaction nonstopmode ./Main.tex || true
	mv *.pdf Proposal.pdf
	rm -f ./*.log ./*.out ./*.aux

test:	clean 
	xelatex -halt-on-error ./main.tex

lint: 
	rm -f ./*.log ./*.out ./*.aux

clean:	lint 
	rm -f ./*.pdf